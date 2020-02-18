package com.example.kashif.android.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.kashif.android.MyApplication;
import com.example.kashif.android.R;
import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.di.components.ActivityComponent;
import com.example.kashif.android.di.components.ConfigPersistentComponent;
import com.example.kashif.android.di.components.DaggerConfigPersistentComponent;
import com.example.kashif.android.di.modules.ActivityModule;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.example.kashif.android.utils.Constants.BUNDLE_KEY_ACTIVITY_ID;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, BaseFragment.FragmentNavigation {

    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    @SuppressLint("UseSparseArrays")
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();
    private ActivityComponent mActivityComponent;
    private long mActivityId;
    private Unbinder mUnBinder;
    public Toolbar mToolbar;
    public TextView actionBarTitle;
    private MenuCreatedListener menuCreatedListener;
    private FrameLayout mainContentContainer;
    @Inject
    DataManager mDataManager;
    private CompositeDisposable mCompositeDisposable;

    public void setActionBarTitle(String actionBarTitle) {
        this.actionBarTitle.setText(actionBarTitle);
    }

    public interface MenuCreatedListener {
        void onCreateOptionsMenu(Menu menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityId = savedInstanceState != null ? savedInstanceState.getLong(BUNDLE_KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        MyApplication.get(this).getComponent().inject(this);

        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(MyApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mActivityComponent = Objects.requireNonNull(configPersistentComponent).activityComponent(new ActivityModule(this));
        setContentView(R.layout.activity_base);

        init();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(BUNDLE_KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    private void init() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mainContentContainer = findViewById(R.id.main_content);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        if (mUnBinder != null)
            mUnBinder.unbind();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuCreatedListener != null)
            menuCreatedListener.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void pushFragment(Fragment fragment) {

    }

    @Override
    public void replaceFragment(Fragment fragment) {

    }

    @Override
    public void navigationTitle(Fragment fragment) {

    }

    @Override
    public void popFragment() {

    }

    @Override
    public void switchTab(int tabNumber) {

    }

    @Override
    public void showDialogFragment(DialogFragment dialogFragment) {

    }

    @Override
    public void clearDialogFragment() {

    }

    @Override
    public void popFragment(int count) {

    }
}
