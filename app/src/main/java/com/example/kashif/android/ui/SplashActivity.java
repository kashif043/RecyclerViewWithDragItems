package com.example.kashif.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kashif.android.MyApplication;
import com.example.kashif.android.R;
import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.ui.home.HomeActivity;
import com.example.kashif.android.utils.CommonUtils;
import com.example.kashif.android.utils.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Maybe;

public final class SplashActivity extends AppCompatActivity {

    @Inject
    DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Maybe.empty()
                .delay(Constants.SPLASH_TIME_OUT, TimeUnit.MILLISECONDS)
                .doOnComplete(() -> {
                    String path = CommonUtils.getMyDocsPath(SplashActivity.this, null);
                    mDataManager.getSharedPrefHelper().storeMyFolderPath(path);
                    Intent home = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(home);
                    finish();
                })
                .subscribe();
    }
}
