package com.example.kashif.android.di.components;

import com.example.kashif.android.di.PerActivity;
import com.example.kashif.android.di.modules.ActivityModule;
import com.example.kashif.android.ui.base.BaseFragment;
import com.example.kashif.android.ui.home.HomeActivity;
import com.example.kashif.android.ui.myDocsActivity.MyDocsActivity;
import com.example.kashif.android.ui.myDocsActivity.myDocsDetailActivity.MyDocsDetailActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseFragment baseFragment);
    void inject(HomeActivity homeActivity);
    void inject(MyDocsActivity myDocsActivity);
    void inject(MyDocsDetailActivity myDocsDetailActivity);
}