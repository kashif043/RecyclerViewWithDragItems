package com.example.kashif.android.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kashif.android.MyApplication;
import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.di.components.ActivityComponent;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    @Inject
    public DataManager dataManager;
    private BaseActivity mActivity;
    private Unbinder mUnBinder;
    private FragmentNavigation mFragNavigation;
    public View parent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.get(getContext()).getComponent().inject(this);
        if (getActivityComponent() != null)
            getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(getLayoutId(), container, false);
        parent.setClickable(true);
        parent.requestFocus();
        mUnBinder = ButterKnife.bind(this, parent);
        setHasOptionsMenu(false);
        initViews(parent);
        return parent;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            mFragNavigation = activity;
        }
    }

    public abstract void initViews(View parentView);

    public abstract int getLayoutId();

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public ActivityComponent getActivityComponent() {
        if (mActivity != null)
            return mActivity.activityComponent();
        return null;
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    protected void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null)
            mUnBinder.unbind();
//        stopSyncService();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void setActionBarTitle(String title) {
        mActivity.setActionBarTitle(title);
    }

    public interface FragmentNavigation {

        void pushFragment(Fragment fragment);

        void replaceFragment(Fragment fragment);

        void navigationTitle(Fragment fragment);

        void popFragment();

        void switchTab(int tabNumber);

        void showDialogFragment(DialogFragment dialogFragment);

        void clearDialogFragment();

        void popFragment(int count);
    }

}
