package com.example.kashif.android.ui.home;

import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.di.ConfigPersistent;
import com.example.kashif.android.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@ConfigPersistent
public final class HomePresenter extends BasePresenter<HomeMvpView> {
    private final DataManager mDataManager;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    HomePresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(HomeMvpView homeMvpView) {
        super.attachView(homeMvpView);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
