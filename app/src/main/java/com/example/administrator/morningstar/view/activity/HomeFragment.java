package com.example.administrator.morningstar.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.administrator.morningstar.R;

/**
 * Created by anson on 2017/4/5.
 */

public class HomeFragment extends BaseFragment{


    @Override
    protected int getViewLayout() {
        return R.layout.fragment_main_home_anim;
    }

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        LottieAnimationView animationView = (LottieAnimationView) getRootView().findViewById(R.id.animation_view);
        animationView.setAnimation("Spider Loader.json");
        animationView.loop(true);
        animationView.playAnimation();
    }
}
