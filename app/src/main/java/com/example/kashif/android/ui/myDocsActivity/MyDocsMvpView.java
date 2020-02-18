package com.example.kashif.android.ui.myDocsActivity;

import com.example.kashif.android.data.MyDocsDataModel;
import com.example.kashif.android.ui.base.MvpView;

import java.util.List;

public interface MyDocsMvpView extends MvpView {
    void onGetDirectoryFilesSuccess(List<MyDocsDataModel> files);
    void onGetDirectoryFilesFailure(String msg);
    void onNewFolderCreateSuccess(String folderName);
    void onDeleteFilesSuccess(String msg);
}
