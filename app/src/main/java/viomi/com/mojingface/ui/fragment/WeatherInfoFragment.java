package viomi.com.mojingface.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import viomi.com.mojingface.R;
import viomi.com.mojingface.base.BaseFragment;
import viomi.com.mojingface.config.MirrorConstans;
import viomi.com.mojingface.databinding.FragmentWeatherinfoBinding;
import viomi.com.mojingface.model.UserEntity;
import viomi.com.mojingface.model.WeatherBean;
import viomi.com.mojingface.ui.activity.SettingActivity;
import viomi.com.mojingface.util.FileUtil;
import viomi.com.mojingface.viewmodel.WeatherInfoViewModel;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: MagicMirror
 * @Package: com.viomi.magicmirror.ui.fragment
 * @ClassName: WeatherInfoFragment
 * @Description: 天气fragment
 * @Author: randysu
 * @CreateDate: 2019/3/21 5:07 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/21 5:07 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public class WeatherInfoFragment extends BaseFragment {

    private FragmentWeatherinfoBinding mBinding;
    private WeatherInfoViewModel viewModel;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weatherinfo, container, false);

        return mBinding.getRoot();
    }

    @Override
    protected void initListener() {
        mBinding.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void init() {
        viewModel = ViewModelProviders.of(this).get(WeatherInfoViewModel.class);
        viewModel.getWeatherInfo().observe(this, weatherObservable);
    }

    @Override
    public void onResume() {
        super.onResume();

        UserEntity user = (UserEntity) FileUtil.getObject(getActivity(), MirrorConstans.USERFILENAME);

        if (user != null) {
            if (TextUtils.isEmpty(user.getViomiHeadImg())) {
                Glide.with(this).load(R.mipmap.default_user_icon).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
            } else {
                Glide.with(this).load(user.getViomiHeadImg()).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
            }
            mBinding.setUsername(user.getViomiNikeName());
        } else {
            Glide.with(this).load(R.mipmap.default_user_icon).apply(new RequestOptions().circleCrop()).into(mBinding.userIcon);
            mBinding.setUsername("未登录");
        }
    }

    Observer<WeatherBean> weatherObservable = new Observer<WeatherBean>() {
        @Override
        public void onChanged(WeatherBean weatherBean) {
            mBinding.setTemp(weatherBean.getTemperature() + "℃");
            mBinding.setWind(weatherBean.getWind());
            mBinding.setWeather(weatherBean.getWeather_dayTime());
        }
    };
}
