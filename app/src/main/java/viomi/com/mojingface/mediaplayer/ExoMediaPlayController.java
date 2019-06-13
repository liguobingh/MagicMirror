package viomi.com.mojingface.mediaplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import viomi.com.mojingface.base.AppManager;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.TimeUtils;

import java.util.List;



/**
 * <p>descript：全局唯一的播放器<p>
 * <p>author：randysu<p>
 * <p>create time：2018/10/25<p>
 * <p>update time：2018/10/25<p>
 * <p>version：1<p>
 */
public class ExoMediaPlayController implements MediaPlayerControlerListener {

    private static final String TAG = ExoMediaPlayController.class.getName();

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private static ExoMediaPlayController instance;

    private Context context;
    private SimpleExoPlayer exoPlayer;
    private int currentPostion = 0; // 当前播放的音乐索引
    private List<Track> playList;
    private boolean isPlaying = false;
    private boolean actionPause = false;
    private DataSource.Factory dataSourceFactory;
    private MediaSource audioSource;
    private OnPlayNewMusicCallback playNewMusicCallback;
    private OnProgressMusicCallback progressMusicCallback;
    private OnPauseMusicCallback pauseMusicCallback;
    private OnResumeMusicCallback resumeMusicCallback;
    private OnStopMusicCallback stopMusicCallback;
    private OnResetViewCallback resetViewCallback;
    private OnPlayErrorCallback playErrorCallback;
    private DefaultTrackSelector trackSelector;
    private ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            LogUtils.i(TAG,"onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            LogUtils.i(TAG,"onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            LogUtils.i(TAG,"onLoadingChanged");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            LogUtils.i(TAG,"onPlayerStateChanged: playWhenReady = "+String.valueOf(playWhenReady)
                    +" playbackState = "+playbackState);
            switch (playbackState){
                case Player.STATE_IDLE:
                    LogUtils.i(TAG,"ExoPlayer idle!");
                    break;
                case Player.STATE_BUFFERING:
                    LogUtils.i(TAG,"Playback buffering!");

                    break;
                case Player.STATE_READY:
                    LogUtils.i(TAG,"ExoPlayer ready! pos: "+exoPlayer.getCurrentPosition()
                            +" max: " + TimeUtils.stringForTime((int)exoPlayer.getDuration()));

                    setProgress();
                    break;
                case Player.STATE_ENDED:
                    LogUtils.i(TAG,"Playback ended!");
                    if (currentPostion == playList.size() - 1) {
                        currentPostion = -1;
                    }
                    nextMusic();
                    break;

                default:
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            LogUtils.i(TAG,"onPlaybackError: " + error.getMessage());

            currentPostion = 0;
            isPlaying = false;

            if (resetViewCallback != null) {
                resetViewCallback.resetViewCallback();
            }

            if (playErrorCallback != null) {
                playErrorCallback.playErorCallback();
            }

        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            LogUtils.i(TAG,"onPositionDiscontinuity");
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            LogUtils.i(TAG,"onPlaybackParametersChanged");
        }

        @Override
        public void onSeekProcessed() {
            LogUtils.i(TAG,"onSeekProcessed");
        }
    };

    public static ExoMediaPlayController getInstance() {
        if (instance == null) {
            synchronized (ExoMediaPlayController.class) {
                if (instance == null) {
                    instance = new ExoMediaPlayController();
                }
            }
        }

        return instance;
    }

    private ExoMediaPlayController() {
        context = MagicMirrorApplication.getAppContext();
    }

    @Override
    public void initPlayer() {
        if (exoPlayer == null) {
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "viomiaiFacePlayer"));
            exoPlayer.addListener(eventListener);
            registerReciver();
        }
    }

    @Override
    public Player getPlayer() {
        return exoPlayer;
    }

    @Override
    public void prepareSource(Uri sourceUri) {
        if (exoPlayer != null) {
            audioSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(sourceUri);
            exoPlayer.prepare(audioSource);
        }
    }

    @Override
    public void setProgress() {
        if (progressMusicCallback != null) {
            progressMusicCallback.progressMusicCallback();
        }
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void playNewMusic() {
        if (exoPlayer != null) {
            isPlaying = true;
            exoPlayer.setPlayWhenReady(isPlaying);
            setProgress();

            if (playNewMusicCallback != null) {
                playNewMusicCallback.playnewMusicCallback();
            }

            if (playList != null && playList.size() > 0 ) {
                if (playList.get(currentPostion) != null) {
                    Intent intent = new Intent(BroadcastAction.SHOWMUSICINMAIN);
                    intent.putExtra("isShowMusic", true);
                    intent.putExtra("musicName", playList.get(currentPostion).getTitle());
                    MagicMirrorApplication.getAppContext().sendBroadcast(intent);
                }
            }
        }
    }

    @Override
    public void pauseMusic() {
        if (exoPlayer != null) {
            isPlaying = false;
            exoPlayer.setPlayWhenReady(isPlaying);

            Intent intent = new Intent(BroadcastAction.SHOWMUSICINMAIN);
//            intent.putExtra("isShowMusic", true);
//            intent.putExtra("musicName", playList.get(currentPostion).getTitle());
            MagicMirrorApplication.getAppContext().sendBroadcast(intent);

            if (pauseMusicCallback != null) {
                pauseMusicCallback.pauseMusicCallback(-1);
            }
        }
    }

    @Override
    public void resumeMusic() {
        if (exoPlayer != null && playList != null && playList.size() > 0) {
            isPlaying = true;
            exoPlayer.setPlayWhenReady(isPlaying);

            if (resumeMusicCallback != null) {
                resumeMusicCallback.resumeMusicCallback();
            }

            if (playList.get(currentPostion) != null) {
                Intent intent = new Intent(BroadcastAction.SHOWMUSICINMAIN);
                intent.putExtra("isShowMusic", true);
                intent.putExtra("musicName", playList.get(currentPostion).getTitle());
                MagicMirrorApplication.getAppContext().sendBroadcast(intent);
            }
        }
    }

    @Override
    public void stopMusic() {
        if (exoPlayer != null) {
            isPlaying = false;
            exoPlayer.stop();
            releasePlayerReasoure();

            Intent intent = new Intent(BroadcastAction.SHOWMUSICINMAIN);
            MagicMirrorApplication.getAppContext().sendBroadcast(intent);

            if (stopMusicCallback != null) {
                stopMusicCallback.stopMusicCallback();
            }
        }
    }

    @Override
    public void preMusic() {
        if (currentPostion <= 0) {
            if (playList != null) {
                currentPostion = playList.size();  // 循环播放
            } else {
                return;
            }
        }
        currentPostion--;

        prepareMusicByPosition(currentPostion);
        playNewMusic();
    }

    @Override
    public void nextMusic() {
        if (playList != null) {
            if (currentPostion >= playList.size() - 1) {
                currentPostion = -1;  // 循环播放
            }
            currentPostion++;

            prepareMusicByPosition(currentPostion);
            playNewMusic();
        }
    }

    @Override
    public void playMusicByIndex(int index) {

    }

    @Override
    public void setActionPause(boolean flag) {
        LogUtils.i(TAG, "flag:" + flag);
        actionPause = flag;
    }

    @Override
    public void resetView() {
        exoPlayer.stop();
        audioSource = null;
        currentPostion = 0;
        playList = null;
        isPlaying = false;

        Intent intent = new Intent(BroadcastAction.SHOWMUSICINMAIN);
        MagicMirrorApplication.getAppContext().sendBroadcast(intent);

        if (resetViewCallback != null) {
            resetViewCallback.resetViewCallback();
        }
    }

    @Override
    public void releasePlayerReasoure() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
            audioSource = null;
            currentPostion = 0;
            playList = null;
            isPlaying = false;
            actionPause = false;
            unRegisterReciver();
        }
    }

    public void setCallback(OnPlayNewMusicCallback playNewMusicCallback,
                            OnProgressMusicCallback progressMusicCallback,
                            OnPauseMusicCallback pauseMusicCallback,
                            OnResumeMusicCallback resumeMusicCallback,
                            OnStopMusicCallback stopMusicCallback,
                            OnResetViewCallback resetViewCallback,
                            OnPlayErrorCallback playErrorCallback) {
        this.playNewMusicCallback = playNewMusicCallback;
        this.progressMusicCallback = progressMusicCallback;
        this.pauseMusicCallback = pauseMusicCallback;
        this.resumeMusicCallback = resumeMusicCallback;
        this.stopMusicCallback = stopMusicCallback;
        this.resetViewCallback = resetViewCallback;
        this.playErrorCallback = playErrorCallback;
    }

    public void freeCallback() {
        playNewMusicCallback = null;
        progressMusicCallback = null;
        pauseMusicCallback = null;
        resumeMusicCallback = null;
        stopMusicCallback = null;
        resetViewCallback = null;
        playErrorCallback = null;
    }

    public void setPlayList(List<Track> playList) {
        if (this.playList != null) {
            this.playList.clear();
        }
        this.playList = playList;
        this.currentPostion = 0;
    }

    public List<Track> getPlayList() {
        return playList;
    }

    public void setCurrentPostion(int position) {
        this.currentPostion = position;
    }

    public int getCurrentPostion() {
        return currentPostion;
    }

    public void prepareMusicByPosition(int currentPostion) {
        if (playList != null) {
            if (currentPostion >= playList.size()) {
                return;
            }

            if (playList.get(currentPostion) != null) {
                String currentSourceUrl = playList.get(currentPostion).getLinkUrl();
                if (!TextUtils.isEmpty(currentSourceUrl)) {
                    prepareSource(Uri.parse(currentSourceUrl));
                }
            }
        }
    }

    private void registerReciver() {
        IntentFilter filter1 = new IntentFilter(BroadcastAction.SPEECHSTART);
        MagicMirrorApplication.getAppContext().registerReceiver(speechStartReciver, filter1);

        IntentFilter filter2 = new IntentFilter(BroadcastAction.DIALOGSTOP);
        MagicMirrorApplication.getAppContext().registerReceiver(dialogStopReciver, filter2);

        IntentFilter filter3 = new IntentFilter(BroadcastAction.PLAYCONTROL_PAUSE);
        MagicMirrorApplication.getAppContext().registerReceiver(pauseReciver, filter3);

        IntentFilter filter4 = new IntentFilter(BroadcastAction.PLAYCONTROL_PLAY);
        MagicMirrorApplication.getAppContext().registerReceiver(playReciver, filter4);

        IntentFilter exitFilter = new IntentFilter(BroadcastAction.PLAYCONTROL_EXIT);
        MagicMirrorApplication.getAppContext().registerReceiver(exitReciver, exitFilter);

        IntentFilter filter8 = new IntentFilter(BroadcastAction.QIUCKORDER_PRE);
        MagicMirrorApplication.getAppContext().registerReceiver(prevReciverQuick, filter8);

        IntentFilter filter7 = new IntentFilter(BroadcastAction.QIUCKORDER_NEXT);
        MagicMirrorApplication.getAppContext().registerReceiver(nextReciverQiuck, filter7);
    }

    private void unRegisterReciver() {
        MagicMirrorApplication.getAppContext().unregisterReceiver(speechStartReciver);
        MagicMirrorApplication.getAppContext().unregisterReceiver(dialogStopReciver);
        MagicMirrorApplication.getAppContext().unregisterReceiver(pauseReciver);
        MagicMirrorApplication.getAppContext().unregisterReceiver(playReciver);
        MagicMirrorApplication.getAppContext().unregisterReceiver(exitReciver);
        MagicMirrorApplication.getAppContext().unregisterReceiver(prevReciverQuick);
        MagicMirrorApplication.getAppContext().unregisterReceiver(nextReciverQiuck);
    }

    private BroadcastReceiver speechStartReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i(TAG, "speechStartReciver-    isPlaying : " + isPlaying);
            if (isPlaying()) {
                pauseMusic();
            }
        }
    };

    private BroadcastReceiver dialogStopReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i(TAG, "speechStopReciver-    isPlaying : " + isPlaying);

            if (!actionPause) {
                if (!isPlaying()) {
                    resumeMusic();
                }
            }
        }
    };

    private BroadcastReceiver pauseReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e(TAG, "pauseReciver- ");
            actionPause = true;
            pauseMusic();
        }
    };

    private BroadcastReceiver playReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e(TAG, "playReciver- ");
            if (!isPlaying()) {
                resumeMusic();
                actionPause = false;
            }
        }
    };

    private BroadcastReceiver exitReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e(TAG, "exitReciver- ");
            stopMusic();
        }
    };

    private BroadcastReceiver prevReciverQuick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i(TAG, "prevReciverQuick- ");

            BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//            if (currentActivity instanceof CookStepActivity) {
//                return;
//            }
            preMusic();
        }
    };

    private BroadcastReceiver nextReciverQiuck = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.i(TAG, "nextReciverQiuck- ");

            BaseActivity currentActivity = AppManager.getInstance().currentActivity();
//            if (currentActivity instanceof CookStepActivity) {
//                return;
//            }
            nextMusic();
        }
    };
}
