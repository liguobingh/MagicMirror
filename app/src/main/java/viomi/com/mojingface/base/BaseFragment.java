package viomi.com.mojingface.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import viomi.com.mojingface.util.LogUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Copyright (C), 2014-2019, 佛山云米科技有限公司
 *
 * @ProjectName: ViomiFaceInWall
 * @Package: com.viomi.viomifaceinwall.base
 * @ClassName: BaseFragment
 * @Description: fragment基类
 * @Author: randysu
 * @CreateDate: 2019/3/2 2:48 PM
 * @UpdateUser:
 * @UpdateDate: 2019/3/2 2:48 PM
 * @UpdateRemark:
 * @Version: 1.0
 */
public abstract class BaseFragment extends Fragment {

    public String TAG = getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.e(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.e(TAG, "onCreateView");
        View view = initView(inflater, container, savedInstanceState);
        initListener();
        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.e(TAG, "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.e(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.e(TAG, "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.e(TAG, "onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy");
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initListener();

    protected abstract void init();

}
