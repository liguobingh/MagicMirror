package viomi.com.mojingface.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.viomi.devicelib.builder.TagViewHelper;
import com.viomi.devicelib.widget.sphere3d.TagCloudView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseFragment;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.ui.activity.MiguMusicMediaPlayActivity;
import viomi.com.mojingface.util.FileUtil;
import viomi.com.mojingface.util.LogUtils;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.ui.fragment
 * @ClassName: DeviceBallFragment
 * @Description: 设备球界面
 * @Author: randysu
 * @CreateDate: 2019/3/21 4:43 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/21 4:43 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class DeviceBallFragment extends BaseFragment {

    private TextView music_view;
    private TagCloudView cloudview;
    private TagViewHelper tagViewHelper;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deviceball, null);
        music_view = view.findViewById(R.id.music_view);
        cloudview = view.findViewById(R.id.iot_device_sphere);
        return view;
    }

    @Override
    protected void initListener() {
        music_view.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MiguMusicMediaPlayActivity.class);
            startActivity(intent);
        });

        Subscription iotSubscription = Observable.interval(0, 4, TimeUnit.HOURS).subscribe(aLong -> {
            if (tagViewHelper != null) {
                tagViewHelper.refreshIotInfos();
            }
        });

        Subscription remoteSubscription = Observable.interval(0, 1, TimeUnit.MINUTES).observeOn(Schedulers.io()).subscribe(aLong -> {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (tagViewHelper != null) {
                        tagViewHelper.remoteDevices();
                    }
                }
            });
        });

        mCompositeSubscription.add(iotSubscription);
        mCompositeSubscription.add(remoteSubscription);
    }

    @Override
    protected void init() {
        tagViewHelper = new TagViewHelper.Builder(getContext())
//                .setRoomRecyclerView(rvRoom) //分区控件
                .setTagCloudView(cloudview) //球控件
                .setShowDid(false)
                .setDeviceType(TagViewHelper.DeviceType.VIOFACE)
                .build();

        cloudview.setIsDrawLine(true);
        cloudview.setScrollSpeed(3f);

        UserEntity user = (UserEntity) FileUtil.getObject(getContext(), MirrorConstans.USERFILENAME);

        if (user != null) {
            if (tagViewHelper != null) {
                tagViewHelper.remoteDevices(user.getViomiToken());
            }
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.SHOWMUSICINMAIN);
        intentFilter.addAction(BroadcastAction.LOGIN_SUCCESS);
        intentFilter.addAction(BroadcastAction.LOGINOUT);
        getContext().registerReceiver(intentReceive, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(intentReceive);

        tagViewHelper.onDestory();
        tagViewHelper = null;

        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("ViomiBallFragment");
    }

    @Override
    public void onPause() {
        super.onPause();

        cloudview.stopRevolve();

        MobclickAgent.onPageEnd("ViomiBallFragment");
    }

    private BroadcastReceiver intentReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadcastAction.LOGIN_SUCCESS.equals(intent.getAction())) {
                LogUtils.i(TAG,"login success!");
                LogUtils.i(TAG,"login success!  userId = " + intent.getStringExtra(BroadcastAction.EXTRA_DATA)
                        + " viomiToken = " + intent.getStringExtra(BroadcastAction.EXTRA_DATA_2));
                String viomiToken = intent.getStringExtra(BroadcastAction.EXTRA_DATA_2);
                if (tagViewHelper != null) {
                    tagViewHelper.remoteDevices(viomiToken);
                }
            } else if (BroadcastAction.LOGINOUT.equals(intent.getAction())) {
                LogUtils.i(TAG,"loginout success!");
                if (tagViewHelper != null) {
                    tagViewHelper.remoteDevices("");
                }
            } else if (BroadcastAction.SHOWMUSICINMAIN.equals(intent.getAction())) {
                String musicName = intent.getStringExtra("musicName");
                boolean isShowMusic = intent.getBooleanExtra("isShowMusic", false);
                if (isShowMusic) {
                     music_view.setVisibility(View.VISIBLE);
                } else {
                    music_view.setVisibility(View.GONE);
                }
                music_view.setText(musicName + "");
            }
        }
    };
}
