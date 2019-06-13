package viomi.com.mojingface.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.miot.api.MiotManager;
import com.miot.common.exception.MiotException;
import com.umeng.analytics.MobclickAgent;
import com.viomi.speech.manager.ViomiSpeechManager;

import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.config.BroadcastAction;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.databinding.ActivityLogoutBinding;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.util.FileUtil;
import viomi.com.mojingface.util.ToastUtil;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: viomi.com.mojingface.ui.activity
 * @ClassName: LogOutActivity
 * @Description: 退出登录界面
 * @Author: randysu
 * @CreateDate: 2019/4/2 10:04 AM
 * @UpdateUser:
 * @UpdateDate: 2019/4/2 10:04 AM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class LogOutActivity extends BaseActivity {

    private ActivityLogoutBinding mBinding;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_logout);
    }

    @Override
    protected void initListener() {
        mBinding.titleLayout.findViewById(R.id.back_icon).setOnClickListener(v -> finish());

        mBinding.loginOutBtn.setOnClickListener(v -> {
            FileUtil.saveObject(LogOutActivity.this, MirrorConstans.USERFILENAME, null);
            ToastUtil.show("已注销登录！");

            MobclickAgent.onProfileSignOff();

            ViomiSpeechManager.getInstance().saveUserInfo("", "", "", "");

            try {
                MiotManager.getPeopleManager().deletePeople();
                sendBroadcast(new Intent(BroadcastAction.LOGINOUT));
            } catch (MiotException e) {
                e.printStackTrace();
            }
            finish();
        });
    }

    @Override
    protected void init() {
        TextView titleView = (TextView) mBinding.titleLayout.findViewById(R.id.title_view);
        titleView.setText(getString(R.string.account_manager));

        Glide.with(this).load(R.mipmap.default_user_icon).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);

        UserEntity user = (UserEntity) FileUtil.getObject(this, MirrorConstans.USERFILENAME);

        if (user != null) {
            if (TextUtils.isEmpty(user.getViomiHeadImg())) {
                Glide.with(this).load(R.mipmap.default_user_icon).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
            } else {
                Glide.with(this).load(user.getViomiHeadImg()).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
            }
            mBinding.setUsername(user.getViomiNikeName());
            mBinding.setUserid("云米ID：" + user.getViomiAccount());
        } else {
            Glide.with(this).load(R.mipmap.default_user_icon).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
            mBinding.setUsername(getString(R.string.not_login_in));
            mBinding.setUserid("云米ID：0");
        }
    }
}
