package viomi.com.mojingface.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.databinding.ActivityLoginBinding;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.ToastUtil;
import viomi.com.mojingface.viewmodel.LoginViewModel;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.ui.activity
 * @ClassName: LoginActivity
 * @Description: 登录界面
 * @Author: randysu
 * @CreateDate: 2019/3/19 4:13 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/19 4:13 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding mBinding;
    private LoginViewModel loginViewModel;

    private String device_id;
    private boolean isFirst;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }

    @Override
    protected void initListener() {
        mBinding.titleLayout.findViewById(R.id.back_icon).setOnClickListener(v -> finish());

        mBinding.downloadApp.setOnClickListener(v -> startActivity(new Intent(this, DownloadViomiActivity.class)));

        mBinding.showGuide.setOnClickListener(v -> startActivity(new Intent(this, LoginGuideActivity.class)));

        mBinding.pass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, DesktopActivity.class);
            startActivity(intent);
            finish();
        });

        mBinding.qecodeImg.setOnClickListener(v -> getQrImg());
    }

    @Override
    protected void init() {
        LogUtils.i(TAG, "init()");
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        TextView titleView = mBinding.titleLayout.findViewById(R.id.title_view);
        titleView.setText("账号登录");

        Intent intent = getIntent();
        isFirst = intent.getBooleanExtra("isFirst", false);

        if (isFirst) {
            mBinding.titleLayout.findViewById(R.id.back_icon).setVisibility(View.GONE);
            mBinding.pass.setVisibility(View.VISIBLE);
        }

        device_id = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + System.currentTimeMillis() / 1000;

        subscribeUi();
    }

    private void subscribeUi() {
        getQrImg();
    }

    private void getQrImg() {
        mBinding.loadingView.setVisibility(View.VISIBLE);
        loginViewModel.getQRImg(device_id).observe(this, qrImgObservable);
    }

    private void checkLogin() {
        loginViewModel.checkLogin(device_id).observe(this, checkLoginObservable);
    }

    Observer<String> qrImgObservable = new Observer<String>() {
        @Override
        public void onChanged(String qrcode_img_url) {
            mBinding.loadingView.setVisibility(View.GONE);
            if (TextUtils.isEmpty(qrcode_img_url)) {
                mBinding.qecodeImg.setImageResource(R.mipmap.login_retry);
            } else {
                Glide.with(LoginActivity.this).load(qrcode_img_url).apply(new RequestOptions().error(R.mipmap.login_retry)).into(mBinding.qecodeImg);
                checkLogin();
            }
        }
    };

    Observer<UserEntity> checkLoginObservable = new Observer<UserEntity>() {
        @Override
        public void onChanged(UserEntity userEntity) {
            if (null != userEntity) {
                ToastUtil.show("登录成功");
                // 登录成功后发送广播
                Intent LoginIntent = new Intent(BroadcastAction.LOGIN_SUCCESS);
                LoginIntent.putExtra(BroadcastAction.EXTRA_DATA, userEntity.getMiId());
                LoginIntent.putExtra(BroadcastAction.EXTRA_DATA_2, userEntity.getViomiToken());
                sendBroadcast(LoginIntent);
                if (isFirst) {
                    Intent intent = new Intent(LoginActivity.this, DesktopActivity.class);
                    startActivity(intent);
                }
                finish();
            } else {
                mBinding.qecodeImg.setImageResource(R.mipmap.login_retry);
                ToastUtil.show("登录失败，请重试");
            }
        }
    };

}
