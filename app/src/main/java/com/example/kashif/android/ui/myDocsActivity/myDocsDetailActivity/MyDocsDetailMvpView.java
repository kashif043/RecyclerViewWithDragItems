package com.example.kashif.android.ui.myDocsActivity.myDocsDetailActivity;

import com.example.kashif.android.data.MyDocsDataModel;
import com.example.kashif.android.ui.base.MvpView;

import java.util.List;

public interface MyDocsDetailMvpView extends MvpView {

    void onGetDirectoryFilesSuccess(List<MyDocsDataModel> files);
    void onGetDirectoryFilesFailure(String msg);
    void onDeleteFilesSuccess(String msg);
}
