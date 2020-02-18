package com.example.kashif.android.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.kashif.android.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class SharedPreferencesModule {

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
