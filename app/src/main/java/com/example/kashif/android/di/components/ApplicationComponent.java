package com.example.kashif.android.di.components;

import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.di.modules.ApplicationModule;
import com.example.kashif.android.di.modules.SharedPreferencesModule;
import com.example.kashif.android.ui.SplashActivity;
import com.example.kashif.android.ui.base.BaseActivity;
import com.example.kashif.android.ui.base.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, SharedPreferencesModule.class})
public interface ApplicationComponent {
    DataManager dataManager();
    void inject(BaseActivity baseActivity);
    void inject(SplashActivity splashActivity);
    void inject(BaseFragment baseFragment);
}

