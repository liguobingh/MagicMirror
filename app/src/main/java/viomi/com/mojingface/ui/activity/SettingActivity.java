package viomi.com.mojingface.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.databinding.ActivitySettingBinding;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.util.FileUtil;
import viomi.com.mojingface.util.PackageUtil;
import viomi.com.mojingface.util.SharedPreferencesUtil;
import viomi.com.mojingface.viewmodel.SettingViewModel;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.ui.activity
 * @ClassName: SettingActivity
 * @Description: 设置界面
 * @Author: randysu
 * @CreateDate: 2019/3/22 9:34 AM
 * @UpdateUser:
 * @UpdateDate: 2019/3/22 9:34 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SettingActivity extends BaseActivity {

    private ActivitySettingBinding mBinding;
    private SettingViewModel settingViewModel;

    private boolean isLogin;
    private AudioManager mAudioManager;
    private int clickcount;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
    }

    @Override
    protected void initListener() {
        mBinding.titleLayout.findViewById(R.id.back_icon).setOnClickListener(v -> finish());

        mBinding.titleLayout.findViewById(R.id.title_view).setOnClickListener(v -> {
            clickcount++;
            if (clickcount == 5) {
                mBinding.debugMode.setVisibility(View.VISIBLE);
            }
        });

        mBinding.loginLayout.setOnClickListener(v -> {
            if (isLogin) {
                startActivity(new Intent(SettingActivity.this, LogOutActivity.class));
            } else {
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            }
        });

        mBinding.wifiSetting.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, AppWifiScanActivity.class)));

        mBinding.versionUpdate.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, ApkUpdateActivity.class)));

        mBinding.sysVoiceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mBinding.mediaVoiceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mBinding.debugSwitchBtn.setOnCheckedChangeListener((buttonView, isChecked) ->
                SharedPreferencesUtil.putData(MirrorConstans.DEBUGMODE_KEY, isChecked));
    }

    @Override
    protected void init() {
        settingViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);

        TextView titleView = mBinding.titleLayout.findViewById(R.id.title_view);
        titleView.setText(R.string.system_title);

        //音量相关
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //当前系统音量
        int currentSysVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        //最大音量
        int maxSysVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        mBinding.sysVoiceBar.setMax(maxSysVolume);
        mBinding.sysVoiceBar.setProgress(currentSysVolume);

        //当前媒体音量
        int currentMediaVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //最大音量
        int maxMediaVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mBinding.mediaVoiceBar.setMax(maxMediaVolume);
        mBinding.mediaVoiceBar.setProgress(currentMediaVolume);

        mBinding.setApkversion("V" + PackageUtil.getVersionName(this));
        mBinding.setSystemversion(Build.DISPLAY);

        initUserInfo();

        settingViewModel.getUserInfo().observe(this, userEntityObservable);

        if ((boolean) SharedPreferencesUtil.getData(MirrorConstans.DEBUGMODE_KEY, false)) {
            mBinding.debugMode.setVisibility(View.VISIBLE);
        }
    }

    Observer<UserEntity> userEntityObservable = new Observer<UserEntity>() {
        @Override
        public void onChanged(UserEntity user) {
            if (user != null) {
                isLogin = true;
                if (TextUtils.isEmpty(user.getViomiHeadImg())) {
                    Glide.with(SettingActivity.this).load(R.mipmap.default_user_icon).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
                } else {
                    Glide.with(SettingActivity.this).load(user.getViomiHeadImg()).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
                }
                mBinding.setUsername(user.getViomiNikeName());
                mBinding.setUserid(user.getViomiAccount());
            } else {
                isLogin = false;
                Glide.with(SettingActivity.this).load(R.mipmap.default_user_icon).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
                mBinding.setUsername(getString(R.string.not_login_in));
                mBinding.setUserid("");
            }
        }
    };

    private void initUserInfo() {
        UserEntity user = (UserEntity) FileUtil.getObject(MagicMirrorApplication.getAppContext(), MirrorConstans.USERFILENAME);

        if (user != null) {
            isLogin = true;
            if (TextUtils.isEmpty(user.getViomiHeadImg())) {
                Glide.with(SettingActivity.this).load(R.mipmap.default_user_icon).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
            } else {
                Glide.with(SettingActivity.this).load(user.getViomiHeadImg()).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
            }
            mBinding.setUsername(user.getViomiNikeName());
            mBinding.setUserid(user.getViomiAccount());
        } else {
            isLogin = false;
            Glide.with(SettingActivity.this).load(R.mipmap.default_user_icon).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
            mBinding.setUsername(getString(R.string.not_login_in));
            mBinding.setUserid("");
        }
    }
}
