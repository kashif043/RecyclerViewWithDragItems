package com.example.kashif.android.di.modules;

import android.app.Application;
import android.content.Context;

import com.example.kashif.android.di.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public final  class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

}
