package com.example.kashif.android.di.modules;

import android.app.Activity;
import android.content.Context;

import com.example.kashif.android.di.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public final class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }




}
