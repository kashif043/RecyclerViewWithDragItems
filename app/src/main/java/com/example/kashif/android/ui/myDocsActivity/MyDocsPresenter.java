package com.example.kashif.android.ui.myDocsActivity;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;

import com.example.kashif.android.R;
import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.data.MyDocsDataModel;
import com.example.kashif.android.di.ConfigPersistent;
import com.example.kashif.android.ui.base.BasePresenter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

@ConfigPersistent
public final class MyDocsPresenter extends BasePresenter<MyDocsMvpView> {
    private final DataManager mDataManager;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    MyDocsPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(MyDocsMvpView myDocsMvpView) {
        super.attachView(myDocsMvpView);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    void getFiles(Context context) {
        List<MyDocsDataModel> docsList = new ArrayList<>();
        File directory = new File(mDataManager.getSharedPrefHelper().getMyFolderPath());
        File[] files = directory.listFiles();
        Timber.d("Size: %s", files.length);
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MyDocsDataModel myDocsDataModel = new MyDocsDataModel();
                if (files[i].isDirectory()) {
                    myDocsDataModel.setName(files[i].getName());
                    myDocsDataModel.setPath(files[i].getAbsolutePath());
                    myDocsDataModel.setSize(Formatter.formatFileSize(context, Long.parseLong(String.valueOf(files[i].length()))));
                    myDocsDataModel.setDate(DateFormat.getDateInstance(DateFormat.LONG).format(new Date(files[i].lastModified())));
                    File[] innerFiles = files[i].listFiles();
                    if (innerFiles.length > 0) {
                        myDocsDataModel.setNumberOfFiles(String.valueOf(innerFiles.length));
                    } else {
                        myDocsDataModel.setNumberOfFiles(String.valueOf("0"));
                    }
                    myDocsDataModel.setChecked(false);
                    myDocsDataModel.setFile(false);
                } else {
                    myDocsDataModel.setName(files[i].getName());
                    myDocsDataModel.setPath(files[i].getAbsolutePath());
                    myDocsDataModel.setSize(Formatter.formatFileSize(context, Long.parseLong(String.valueOf(files[i].length()))));
                    myDocsDataModel.setDate(DateFormat.getDateInstance(DateFormat.LONG).format(new Date(files[i].lastModified())));
                    myDocsDataModel.setNumberOfFiles(String.valueOf("0"));
                    myDocsDataModel.setChecked(false);
                    myDocsDataModel.setFile(true);
                }

                docsList.add(myDocsDataModel);

                Timber.d("FileName:%s", files[i].getName());
            }
            getMvpView().onGetDirectoryFilesSuccess(docsList);
        } else {
            getMvpView().onGetDirectoryFilesFailure(String.valueOf(R.string.error));
        }
    }

    void deleteSelectedFiles(List<MyDocsDataModel> pdfFiles) {

        for (int i = 0; i < pdfFiles.size(); i++) {
            if (!pdfFiles.get(i).isFile()) {
                File dir = new File(pdfFiles.get(i).getPath());
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int k = 0; k < children.length; k++) {
                        new File(dir, children[k]).delete();
                        Timber.d("Image File Deleted Successfully");
                    }
                    try {
                        FileUtils.deleteDirectory(dir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                File imageFile = new File(pdfFiles.get(i).getPath());
                if (imageFile.exists()) {
                    boolean isDeleted = imageFile.delete();
                    Timber.d("Image File Deleted Successfully - > %s", isDeleted);
                }
            }

        }
        getMvpView().onDeleteFilesSuccess(String.valueOf(R.string.success));

    }

    void createFolder(String name) {
        File rootDir = new File(mDataManager.getSharedPrefHelper().getMyFolderPath());
        File newFolder = null;
        newFolder = new File(rootDir.getAbsolutePath() + "/" + name);

        if (!newFolder.exists())
            newFolder.mkdir();

        getMvpView().onNewFolderCreateSuccess(newFolder.getAbsolutePath());
    }
}
