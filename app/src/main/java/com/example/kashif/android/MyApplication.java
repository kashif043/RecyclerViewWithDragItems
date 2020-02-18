package com.example.kashif.android;

import android.app.Application;
import android.content.Context;

import com.example.kashif.android.di.components.ApplicationComponent;
import com.example.kashif.android.di.components.DaggerApplicationComponent;
import com.example.kashif.android.di.modules.ApplicationModule;
import com.example.kashif.android.di.modules.SharedPreferencesModule;

import timber.log.Timber;

public final class MyApplication extends Application {

    ApplicationComponent mApplicationComponent;
    private static MyApplication mInstance;

    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        mInstance = this;
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .sharedPreferencesModule(new SharedPreferencesModule())
                    .build();
        }
        return mApplicationComponent;
    }


    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

}
