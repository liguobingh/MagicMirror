package viomi.com.mojingface.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseActivity;
import viomi.com.mojingface.base.MagicMirrorApplication;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.databinding.ActivityApkupdateBinding;
import viomi.com.mojingface.model.DownloadInfoBean;
import viomi.com.mojingface.util.LogUtils;
import viomi.com.mojingface.util.PackageUtil;
import viomi.com.mojingface.util.SharedPreferencesUtil;
import viomi.com.mojingface.viewmodel.ApkUpdateViewModel;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: viomi.com.mojingface.ui.activity
 * @ClassName: ApkUpdateActivity
 * @Description: Apk检查更新界面
 * @Author: randysu
 * @CreateDate: 2019/4/2 1:54 PM
 * @UpdateUser:
 * @UpdateDate: 2019/4/2 1:54 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ApkUpdateActivity extends BaseActivity {

    private ActivityApkupdateBinding mBinding;
    private ApkUpdateViewModel apkUpdateViewModel;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_apkupdate);
    }

    @Override
    protected void initListener() {
        mBinding.titleLayout.findViewById(R.id.back_icon).setOnClickListener(v -> finish());

        mBinding.updateLayout.setOnClickListener(v -> {
            mBinding.updateBtn.setVisibility(View.GONE);
            mBinding.checkUpdateProgressbar.setVisibility(View.VISIBLE);

            apkUpdateViewModel.checkUpdate().observe(this, updateResultObservable);
        });

        mBinding.cancle.setOnClickListener(v -> {
            mBinding.updateLayout.setEnabled(true);
            mBinding.updateProgressLayout.setVisibility(View.GONE);
            mBinding.updateProgressBar.setProgress(0);
            mBinding.updateProgress.setText("正在解析升级文件...");

            apkUpdateViewModel.cancelDownloadTask();
        });
    }

    @Override
    protected void init() {
        apkUpdateViewModel = ViewModelProviders.of(this).get(ApkUpdateViewModel.class);

        TextView titleText = mBinding.titleLayout.findViewById(R.id.title_view);
        titleText.setText("应用版本");

        mBinding.setApkversion("当前版本：V" + PackageUtil.getVersionName(this));

        apkUpdateViewModel.getDownloadInfo().observe(this, downloadInfoObservable);
    }

    Observer<String> updateResultObservable = new Observer<String>() {
        @Override
        public void onChanged(String result) {
            if (TextUtils.isEmpty(result)) {
                LogUtils.i(TAG, "检查应用更新失败");
                mBinding.updateBtn.setVisibility(View.VISIBLE);
                mBinding.checkUpdateProgressbar.setVisibility(View.GONE);

                mBinding.updateBtn.setEnabled(true);
                mBinding.updateProgressLayout.setVisibility(View.GONE);
                mBinding.checkUpdateProgressbar.setProgress(0);
                mBinding.updateProgress.setText("正在解析升级文件...");

                Toast.makeText(ApkUpdateActivity.this, "检查应用更新失败", Toast.LENGTH_SHORT).show();
            } else {
                LogUtils.i(TAG, "检查应用更新成功");
                mBinding.updateBtn.setVisibility(View.VISIBLE);
                mBinding.checkUpdateProgressbar.setVisibility(View.GONE);

                if (apkUpdateViewModel.hasNewestVersion(result)) {
                    apkUpdateViewModel.showNewVersionDialog();
                } else {
                    Toast.makeText(MagicMirrorApplication.getAppContext(), "当前为最新版本", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    Observer<DownloadInfoBean> downloadInfoObservable = new Observer<DownloadInfoBean>() {

        @Override
        public void onChanged(DownloadInfoBean downloadInfoBean) {
            if (DownloadInfoBean.DOWNLOADSTATUS_ANALYSISING == downloadInfoBean.getDownloadStatus()) {
                mBinding.updateLayout.setEnabled(false);
                mBinding.updateProgressLayout.setVisibility(View.VISIBLE);
                mBinding.cancle.setVisibility(View.GONE);
                mBinding.updateProgress.setText("正在解析升级文件...");
            } else if (DownloadInfoBean.DOWNLOADSTATUS_PROGRESSING == downloadInfoBean.getDownloadStatus()) {
                mBinding.updateLayout.setEnabled(false);
                mBinding.cancle.setVisibility(View.VISIBLE);
                mBinding.updateProgressLayout.setVisibility(View.VISIBLE);
                mBinding.updateProgressBar.setVisibility(View.VISIBLE);
                mBinding.updateProgressBar.setProgress(new Long(downloadInfoBean.getProgress()).intValue());
                mBinding.updateProgress.setText("下载进度：" + downloadInfoBean.getProgress() + "%  "
                        + "(" + downloadInfoBean.getCurrentBytes() + "/" + downloadInfoBean.getContentLength()
                        + "  " + downloadInfoBean.getSpeed() + "kb/s)");
            } else {
                mBinding.updateLayout.setEnabled(true);
                mBinding.updateProgressLayout.setVisibility(View.GONE);

                if (apkUpdateViewModel.isApkExits()) {
                    apkUpdateViewModel.startToInstallApk((String)SharedPreferencesUtil.getData(MirrorConstans.NEWAPKVERSIONNAME_KEY, ""));
                }
            }
        }
    };

}
