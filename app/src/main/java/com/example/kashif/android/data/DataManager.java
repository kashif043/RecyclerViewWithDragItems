package com.example.kashif.android.data;

import com.example.kashif.android.data.local.SharedPrefHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class DataManager {

    private SharedPrefHelper sharedPrefHelper;

    @Inject
    public DataManager(SharedPrefHelper sharedPrefHelper) {
        this.sharedPrefHelper = sharedPrefHelper;
    }

    public SharedPrefHelper getSharedPrefHelper() {
        return sharedPrefHelper;
    }

}
