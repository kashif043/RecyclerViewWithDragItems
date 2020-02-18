package com.example.kashif.android.data.local;

import android.content.SharedPreferences;
import com.example.kashif.android.utils.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class SharedPrefHelper implements UserDataHelper {

    private SharedPreferences preferences;

    @Inject
    SharedPrefHelper(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    private void store(Constants.PersistenceKey key, String value) {
        preferences.edit().putString(key.toString(), value).apply();
    }

    private String retrieve(Constants.PersistenceKey key) {
        return preferences.getString(key.toString(), "");
    }

    private void storeBoolean(Constants.PersistenceKey key, boolean value) {
        preferences.edit().putBoolean(key.toString(), value).apply();
    }

    private boolean retrieveBoolean(Constants.PersistenceKey key) {
        return preferences.getBoolean(key.toString(), false);
    }

    private void storeInt(Constants.PersistenceKey key, int value) {
        preferences.edit().putInt(key.toString(), value).apply();
    }

    private int retrieveInt(Constants.PersistenceKey key) {
        return preferences.getInt(key.toString(), 0);
    }

    @Override
    public void storeMyFolderPath(String path) {
        store(Constants.PersistenceKey.MY_DOCS_PATH, path);
    }

    @Override
    public String getMyFolderPath() {
        return retrieve(Constants.PersistenceKey.MY_DOCS_PATH);
    }
}
