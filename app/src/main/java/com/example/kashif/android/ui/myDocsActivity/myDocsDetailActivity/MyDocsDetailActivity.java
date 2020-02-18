package com.example.kashif.android.ui.myDocsActivity.myDocsDetailActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.kashif.android.R;
import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.data.MyDocsDataModel;
import com.example.kashif.android.databinding.ActivityMyDocsDetailsBinding;
import com.example.kashif.android.ui.base.BaseActivity;
import com.example.kashif.android.ui.myDocsActivity.MyDocsActivity;
import com.example.kashif.android.ui.myDocsActivity.MyDocsAdapter;
import com.example.kashif.android.utils.CommonUtils;
import com.example.kashif.android.utils.Constants;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

public final class MyDocsDetailActivity extends BaseActivity implements MyDocsDetailMvpView, MyDocsAdapter.MyDocsClickListener {

    @Inject
    DataManager mDataManager;
    @Inject
    MyDocsDetailPresenter myDocsDetailPresenter;
    @Inject
    MyDocsAdapter myDocsAdapter;

    ActivityMyDocsDetailsBinding binding;
    private String folderName, folderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_docs_details);
        activityComponent().inject(this);
        binding.setListener(this);
        folderName = getIntent().getStringExtra(Constants.NAME);
        folderPath = getIntent().getStringExtra(Constants.PATH);
        initilzeStuff();
    }

    private void initilzeStuff() {
        myDocsDetailPresenter.attachView(this);
        binding.tvToolBar.setText(folderName);
        binding.rvMyDocs.setAdapter(myDocsAdapter);
        myDocsAdapter.setClickListener(this);
        binding.pbMyDocs.setVisibility(View.VISIBLE);
        myDocsDetailPresenter.getFiles(MyDocsDetailActivity.this, folderPath);

        binding.searchFiles.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myDocsAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void goToHome() {
        super.onBackPressed();
    }

    public void showSearchView() {
        binding.searchBarLayout.setVisibility(View.VISIBLE);
    }

    public void hideSearchView() {
        binding.searchFiles.setText("");
        myDocsAdapter.getFilter().filter("");
        binding.searchBarLayout.setVisibility(View.GONE);
    }

    public void selectAll() {
        binding.selectAllIv.setVisibility(View.GONE);
        binding.deSelectAllIv.setVisibility(View.VISIBLE);
        myDocsAdapter.selectAllFiles(true);
    }

    public void deSelectAll() {
        binding.selectAllIv.setVisibility(View.VISIBLE);
        binding.deSelectAllIv.setVisibility(View.GONE);
        myDocsAdapter.selectAllFiles(false);
    }

    public void deleteSelectedFiles() {
        List<MyDocsDataModel> pdfFiles = myDocsAdapter.getSelectedFiles();
        if (pdfFiles.size() > 0) {
            myDocsDetailPresenter.deleteSelectedFiles(pdfFiles);
        } else {
            CommonUtils.showToast(MyDocsDetailActivity.this, R.string.select_files_delete);
        }
    }

    @Override
    public void navigationTitle(Fragment fragment) {

    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myDocsDetailPresenter.detachView();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFolderNameClicked(MyDocsDataModel files) {

    }

    @Override
    public void onGetDirectoryFilesSuccess(List<MyDocsDataModel> files) {
        System.out.println("FILES FETECHED FROM DIRECTORY");
        System.out.println(files);

        binding.pbMyDocs.setVisibility(View.GONE);
        if (files.size() > 0) {
            Collections.sort(files, (file1, file2) -> Boolean.compare(file1.isFile(), file2.isFile()));
            myDocsAdapter.setList(files);
            binding.rvMyDocs.setVisibility(View.VISIBLE);
            binding.emptyView.setVisibility(View.GONE);
        } else {
            binding.rvMyDocs.setVisibility(View.GONE);
            binding.emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetDirectoryFilesFailure(String msg) {
        binding.pbMyDocs.setVisibility(View.GONE);
        binding.rvMyDocs.setVisibility(View.GONE);
        binding.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeleteFilesSuccess(String msg) {
        myDocsAdapter.clearLists();
        myDocsDetailPresenter.getFiles(MyDocsDetailActivity.this, folderPath);
    }

    @Override
    public void onDeleteFolderClicked(MyDocsDataModel files) {

        if (!files.isFile()) {
            File dir = new File(files.getPath());
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int k = 0; k < children.length; k++) {
                    new File(dir, children[k]).delete();
                    Timber.d("Image File Deleted");
                }
                try {
                    FileUtils.deleteDirectory(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        myDocsAdapter.clearLists();
        myDocsDetailPresenter.getFiles(MyDocsDetailActivity.this, folderPath);

    }

    @Override
    public void onDeleteFileClicked(MyDocsDataModel files) {
        File imageFile = new File(files.getPath());
        if (imageFile.exists()) {
            boolean isDeleted = imageFile.delete();
            Timber.d("Image File Deleted - > %s", isDeleted);
        }

        myDocsAdapter.clearLists();
        myDocsDetailPresenter.getFiles(MyDocsDetailActivity.this, folderPath);
    }
}
