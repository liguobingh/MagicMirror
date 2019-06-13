package viomi.com.mojingface.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import me.wcy.lrcview.LrcView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Observable;
import rx.schedulers.Schedulers;
import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.http.HttpApi;
import viomi.com.mojingface.http.service.migu.MiguLoader;
import viomi.com.mojingface.mediaplayer.ExoMediaPlayController;
import viomi.com.mojingface.mediaplayer.MediaPlayerControlerListener;
import viomi.com.mojingface.mediaplayer.Track;
import viomi.com.mojingface.mediaplayer.musicstrategy.MiguMusicApiCallback;
import viomi.com.mojingface.mediaplayer.musicstrategy.MusicContext;
import viomi.com.mojingface.mediaplayer.musicstrategy.MusicListByAlbumStrategy;
import viomi.com.mojingface.mediaplayer.musicstrategy.MusicListBySheetStrategy;
import viomi.com.mojingface.mediaplayer.musicstrategy.MusicListBySingerStrategy;
import viomi.com.mojingface.mediaplayer.musicstrategy.MusicListBySongStrategy;
import viomi.com.mojingface.mediaplayer.musicstrategy.MusicListStrategy;
import viomi.com.mojingface.speech.skill.MusicSkill;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.ParsePlayList;
import viomi.com.mojingface.util.SnackbarUtil;
import viomi.com.mojingface.util.StringUtil;
import viomi.com.mojingface.widget.GlideRoundTransform;
import viomi.com.wifilibrary.wifimodel.WifiScanActivity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * <p>descript：咪咕音乐界面<p>
 * <p>author：randysu<p>
 * <p>create time：2018/11/7<p>
 * <p>update time：2018/11/7<p>
 * <p>version：1<p>
 */
public class MiguMusicMediaPlayActivity extends BaseActivity {

    public static final String MUSIC_DIRECTIVE = "music_directive";
    public static final String PLAYTYPE = "playtype";

    private View contentPanel;
    private ImageView back_icon;
    private ImageView img_cover;
    private TextView music_title;
    private TextView singer;
    private LrcView lrc_view;
    private TextView lrc_tips;
    private SeekBar music_progress;
    private TextView time_progress_tv;
    private ImageView pre_btn;
    private ImageView play_btn;
    private ImageView next_btn;

    private int getMusicLrcInfoCount = 0;
    private String playType;
    private int searchType; // 查询歌曲类别
    private String searchKey; // 查询关键信息
    private String currentImg;
    private ExoMediaPlayController playController;
    private boolean isSeeking = false;
    private boolean hasError = false;
    private Call getLrcCall;

    private MiguLoader miguLoader;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_migumusic_media_play);

        contentPanel = findViewById(R.id.contentPanel);
        back_icon = findViewById(R.id.back_icon);
        img_cover = findViewById(R.id.img_cover);
        music_title = findViewById(R.id.music_title);
        singer = findViewById(R.id.singer);
        lrc_view = findViewById(R.id.lrc_view);
        lrc_tips = findViewById(R.id.lrc_tips);
        music_progress = findViewById(R.id.music_progress);
        time_progress_tv = findViewById(R.id.time_progress_tv);
        play_btn = findViewById(R.id.play_btn);
        pre_btn = findViewById(R.id.pre_btn);
        next_btn = findViewById(R.id.next_btn);
    }

    @Override
    protected void initListener() {
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playController.isPlaying()) {
                    playController.releasePlayerReasoure();
                }
                playController.freeCallback();
                finish();
            }
        });

        music_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }

                String currentTime = StringUtil.musicFormatTime(progress * 1000);
                String endTime = StringUtil.musicFormatTime((int) playController.getPlayer().getDuration());
                time_progress_tv.setText(currentTime + "/" + endTime);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isSeeking) {
                    int progress = music_progress.getProgress();
                    playController.getPlayer().seekTo(progress * 1000);
                    if (!playController.isPlaying()) {
                        playController.resumeMusic();
                    }
                    isSeeking = false;
                }
            }
        });

        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playController.preMusic();
            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playController.isPlaying()) {
                    playController.setActionPause(true);
                    playController.pauseMusic();
                } else {
                    playController.setActionPause(false);
                    playController.resumeMusic();
                }
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playController.nextMusic();
            }
        });

    }

    @Override
    protected void init() {

        miguLoader = new MiguLoader();

        IntentFilter netWorkConnectedFilter = new IntentFilter(BroadcastAction.NETWORK_REAL_CONNECTED);
        registerReceiver(netWorkConnectedReceive, netWorkConnectedFilter);

        playController = ExoMediaPlayController.getInstance();
        playController.initPlayer();
        playController.setCallback(playNewMusicCallback,
                progressMusicCallback,
                pauseMusicCallback,
                resumeMusicCallback,
                stopMusicCallback,
                resetViewCallback,
                playErrorCallback);

        parseIntentInfo(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        LogUtils.i(TAG, "我是再次进入界面的日志");

        parseIntentInfo(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(netWorkConnectedReceive);

        playController.freeCallback();

        if (getLrcCall != null) {
            getLrcCall.cancel();
        }
    }

    private void parseIntentInfo(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String newPlayType = bundle.getString(PLAYTYPE, "");
            int newSearchType = bundle.getInt(MusicSkill.SEARCHTYPE, 0);
            String newSearchKey = bundle.getString(MusicSkill.SEARCHKEY, "");
            if (newPlayType.equals(playType) && searchType == newSearchType && newSearchKey.equals(searchKey)) {
                switchPlayView(true);
                return;
            } else {
                playType = newPlayType;
                searchType = newSearchType;
                searchKey = newSearchKey;

                // 重置播放界面
                playController.resetView();
            }
            String music_directive = bundle.getString(MUSIC_DIRECTIVE, "");

            LogUtils.i(TAG, "playType: " + playType);
            LogUtils.i(TAG, "searchType: " + searchType);
            LogUtils.i(TAG, "searchKey: " + searchKey);
            LogUtils.i(TAG, "music_directive: " + music_directive);

            List<Track> playList;

            if (playType != null) {
                switch (playType) {
                    case "MUSIC_ID":
                        MusicContext strategy = null;
                        if (MusicSkill.SEARCHTYPE_BYSONG == searchType) {
                            strategy = new MusicContext(new MusicListBySongStrategy());
                            strategy.getPlayList(miguMusicApiCallback, searchKey);
                        } else if (MusicSkill.SEARCHTYPE_BYALBUM == searchType) {
                            strategy = new MusicContext(new MusicListByAlbumStrategy());
                            strategy.getPlayList(miguMusicApiCallback, searchKey);
                        } else if (MusicSkill.SEARCHTYPE_BYARTIST == searchType) {
                            strategy = new MusicContext(new MusicListBySingerStrategy());
                            strategy.getPlayList(miguMusicApiCallback, searchKey);
                        } else if (MusicSkill.SEARCHTYPE_BYSHEETID == searchType) {
                            strategy = new MusicContext(new MusicListBySheetStrategy());
                            strategy.getPlayList(miguMusicApiCallback, searchKey);
                        } else {
                            strategy = new MusicContext(new MusicListStrategy());
                            strategy.getPlayList(miguMusicApiCallback);
                        }
                        break;
                    case "TOY_NEWS_ID":
                        playList = ParsePlayList.parseToyNews(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    case "LT_NEWS_ID":
                        playList = ParsePlayList.parseLTNews(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    case "TOY_STORY_ID":
                        playList = ParsePlayList.parseToyStory(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    case "TOY_FUN_ID":
                        playList = ParsePlayList.parseContent(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    case "CHINESE_STUDY_ID":
                        playList = ParsePlayList.parseContent(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    case "INSIDE_MUSIC_ID":
                        playList = ParsePlayList.parseInsideMusicContent(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    case "FOLK_ART_ID":
                        playList = ParsePlayList.parseFolkArtContent(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    case "FUN_ID":
                        playList = ParsePlayList.parseFunContent(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    case "CHILD_SONG_ID":
                        playList = ParsePlayList.parseChildSongContent(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    case "STORY_ID":
                        playList = ParsePlayList.parseStoryContent(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                    default:
                        playList = ParsePlayList.parseContent(music_directive);
                        playController.setPlayList(playList);
                        switchPlayView(false);
                        break;
                }
            }
        } else {
            switchPlayView(true);
        }
    }

    /**
     * @param isSameIntent  指定是否为新intent触发的界面变化
     *                      如果是首页进入，当作是相同intent，继续播放
     */
    private void switchPlayView(boolean isSameIntent) {
        if (!isDestroyed() && !isFinishing()) {
            if (playController.isPlaying() && isSameIntent) { // 正在播放进入界面
                play_btn.setImageResource(R.mipmap.icon_pause);
                playController.setProgress();

                play_btn.setEnabled(true);
                next_btn.setEnabled(true);
                pre_btn.setEnabled(true);
            } else {
                if (playController.getPlayList() == null) {
                    return;
                }
                LogUtils.i(TAG, "playList.size=" + playController.getPlayList().size());
                if (playController.getPlayList().size() <= 0) {
                    lrc_view.setVisibility(View.GONE);
                    lrc_tips.setVisibility(View.VISIBLE);
                    lrc_tips.setText("歌曲列表获取失败，请再次呼唤\"云米小V\"");

                    play_btn.setEnabled(false);
                    next_btn.setEnabled(false);
                    pre_btn.setEnabled(false);

                    return;
                }

                play_btn.setEnabled(true);
                next_btn.setEnabled(true);
                pre_btn.setEnabled(true);

                int randomIndex = 0;
                if (searchType == MusicSkill.SEARCHTYPE_NONE) {
                    randomIndex = new Random().nextInt(playController.getPlayList().size());
                }
                playController.setCurrentPostion(randomIndex);
                playController.prepareMusicByPosition(randomIndex);
                playController.playNewMusic();
            }

            showLrcInfos();

            showViewInfos();
        }
    }

    private void getMusicLrcInfo(String lrcUrl) {
        HttpApi.getRequest(lrcUrl, new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                if (!isDestroyed() && !isFinishing()) {
                    getMusicLrcInfoCount++;
                    if (getMusicLrcInfoCount <= 5) {
                        getMusicLrcInfo(lrcUrl);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lrc_view.setVisibility(View.GONE);
                                lrc_tips.setVisibility(View.VISIBLE);
                                lrc_tips.setText("歌词加载失败");
                            }
                        });
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!isDestroyed() && !isFinishing()) {
                    try {
                        String lrcSource = response.body().string();
                        LogUtils.i(TAG, "body:" + lrcSource);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lrc_view.setVisibility(View.VISIBLE);
                                lrc_tips.setVisibility(View.GONE);
                                lrc_tips.setText("歌曲加载中...");

                                LogUtils.i(TAG, "body:" + lrcSource);

                                lrc_view.loadLrc(lrcSource);
                                lrc_view.updateTime(0);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        response.body().close();
                    }
                }
            }
        });
    }

    private void showLrcInfos() {
        if (playController.getPlayList() != null && playController.getPlayList().size() > 0) {
            Track track = playController.getPlayList().get(playController.getCurrentPostion());
            if (track != null) {
                if (TextUtils.isEmpty(track.getLrcUrl())) {
                    lrc_view.setVisibility(View.GONE);
                    lrc_tips.setVisibility(View.VISIBLE);
                    lrc_tips.setText("当前类别不支持歌词显示");
                } else {
                    lrc_view.setVisibility(View.GONE);
                    lrc_tips.setVisibility(View.VISIBLE);
                    lrc_tips.setText("歌词加载中...");

                    String lrcUrl = track.getLrcUrl();
                    LogUtils.i(TAG, "lrcUrl:" + lrcUrl);

                    getMusicLrcInfo(lrcUrl);
                }
            } else {
                lrc_view.setVisibility(View.GONE);
                lrc_tips.setVisibility(View.VISIBLE);
                lrc_tips.setText("当前类别不支持歌词显示");
            }
        }
    }

    private void showViewInfos() {
        if (playController.getPlayList() != null && playController.getPlayList().size() > 0) {
            Track track = playController.getPlayList().get(playController.getCurrentPostion());
            if (track != null) {
                music_title.setText(track.getTitle());
                singer.setText(track.getSubTitle());
                if (currentImg!=null&&currentImg.equals(track.getImageUrl())) {
                    return;
                }
                currentImg = track.getImageUrl();
                Glide.with(this)
                        .load(track.getImageUrl())
                        .transition(withCrossFade())
                        .apply(new RequestOptions()
                                .transform(new GlideRoundTransform(this,getResources().getInteger(R.integer.img_cover_corner)))
                                .error(R.mipmap.img_cover_placeholder))
                        .into(img_cover);
            }
        }
    }

    private boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private MediaPlayerControlerListener.OnPlayNewMusicCallback playNewMusicCallback = new MediaPlayerControlerListener.OnPlayNewMusicCallback() {
        @Override
        public void playnewMusicCallback() {
            play_btn.setImageResource(R.mipmap.icon_pause);

            switchPlayView(true);
        }
    };

    private MediaPlayerControlerListener.OnProgressMusicCallback progressMusicCallback = new MediaPlayerControlerListener.OnProgressMusicCallback() {
        @Override
        public void progressMusicCallback() {
            if (playController.getPlayer() != null) {
                music_progress.setMax((int) playController.getPlayer().getDuration() / 1000);
                String currentTime = StringUtil.musicFormatTime((int) playController.getPlayer().getCurrentPosition());
                String endTime = StringUtil.musicFormatTime((int) playController.getPlayer().getDuration());
                time_progress_tv.setText(currentTime + "/" + endTime);

                Observable.interval(1, TimeUnit.SECONDS).observeOn(Schedulers.io()).subscribe(aLong -> {
                    if (playController.isPlaying()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hasError = false;

                                if (!isSeeking) {
                                    int mCurrentPosition = (int) playController.getPlayer().getCurrentPosition() / 1000;
                                    int mBufferedPosition = (int) playController.getPlayer().getBufferedPosition() / 1000;
                                    music_progress.setProgress(mCurrentPosition);
                                    music_progress.setSecondaryProgress(mBufferedPosition);
                                    String currentTime = StringUtil.musicFormatTime((int) playController.getPlayer().getCurrentPosition());
                                    String endTime = StringUtil.musicFormatTime((int) playController.getPlayer().getDuration());
                                    time_progress_tv.setText(currentTime + "/" + endTime);
                                }

                                lrc_view.updateTime(playController.getPlayer().getCurrentPosition());
                            }
                        });

                }});

            }
        }
    };

    private MediaPlayerControlerListener.OnPauseMusicCallback pauseMusicCallback = new MediaPlayerControlerListener.OnPauseMusicCallback() {
        @Override
        public void pauseMusicCallback(int playStatus) {
            play_btn.setImageResource(R.mipmap.icon_play);

        }
    };

    private MediaPlayerControlerListener.OnResumeMusicCallback resumeMusicCallback = new MediaPlayerControlerListener.OnResumeMusicCallback() {
        @Override
        public void resumeMusicCallback() {
            play_btn.setImageResource(R.mipmap.icon_pause);
        }
    };

    private MediaPlayerControlerListener.OnStopMusicCallback stopMusicCallback = new MediaPlayerControlerListener.OnStopMusicCallback() {
        @Override
        public void stopMusicCallback() {
            if (!playController.isPlaying()) {
                playController.releasePlayerReasoure();
            }
            playController.freeCallback();
            finish();
        }
    };

    private MediaPlayerControlerListener.OnResetViewCallback resetViewCallback = new MediaPlayerControlerListener.OnResetViewCallback() {
        @Override
        public void resetViewCallback() {

            img_cover.setImageResource(R.mipmap.img_cover_placeholder);
            music_title.setText("歌曲名");
            singer.setText("歌手");
            lrc_view.setVisibility(View.GONE);
            lrc_tips.setVisibility(View.VISIBLE);
            lrc_tips.setText("歌曲加载中...");

            music_progress.setProgress(0);
            music_progress.setSecondaryProgress(0);
            time_progress_tv.setText("00:00/00:00");
            play_btn.setImageResource(R.mipmap.icon_play);
        }
    };

    private MediaPlayerControlerListener.OnPlayErrorCallback playErrorCallback = new MediaPlayerControlerListener.OnPlayErrorCallback() {
        @Override
        public void playErorCallback() {

            hasError = true;

            SnackbarUtil.make(contentPanel, "播放出错，请检查网络连接是否设置正确", 0, Snackbar.LENGTH_INDEFINITE,
                    ContextCompat.getColor(MagicMirrorApplication.getAppContext(), R.color.snarkbar_warm_bg_color), "设置",
                    ContextCompat.getColor(MagicMirrorApplication.getAppContext(), R.color.btn_blue), new SnackbarUtil.ActionCallback() {
                        @Override
                        public void onAction(View view) {
                            Intent wifiSettingIntent = new Intent(activity, WifiScanActivity.class);
                            activity.startActivity(wifiSettingIntent);
                        }
                    });
        }
    };

    private MiguMusicApiCallback miguMusicApiCallback = new MiguMusicApiCallback<Track>() {
        @Override
        public void apiOnStart() {
            LogUtils.i(TAG, "apiOnStart");
        }

        @Override
        public void apiOnSuccess(List<Track> trackList) {
            LogUtils.i(TAG, "trackList size:" + trackList.size());
            if (isMainThread()) {
                playController.setPlayList(trackList);
                switchPlayView(false);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playController.setPlayList(trackList);
                        switchPlayView(false);
                    }
                });
            }
        }

        @Override
        public void apiOnFailed(String s, String s1) {
            LogUtils.i(TAG, "apiOnFailed s:" + s + "  s1:" + s1);

            if (isMainThread()) {
                lrc_view.setVisibility(View.GONE);
                lrc_tips.setVisibility(View.VISIBLE);
                lrc_tips.setText("歌曲列表获取失败，请再次呼唤\"云米小V\"\n错误信息：" + s + "  " + s1);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lrc_view.setVisibility(View.GONE);
                        lrc_tips.setVisibility(View.VISIBLE);
                        lrc_tips.setText("歌曲列表获取失败，请再次呼唤\"云米小V\"\n错误信息：" + s + "  " + s1);
                    }
                });
            }

        }

        @Override
        public void apiOnFinish() {
            LogUtils.i(TAG, "apiOnFinish");
        }
    };

    private BroadcastReceiver netWorkConnectedReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isDestroyed() && !isFinishing() && hasError) {
                switchPlayView(true);
            }
        }
    };

}
