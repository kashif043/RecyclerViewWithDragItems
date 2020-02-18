package com.example.kashif.android.ui.myDocsActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kashif.android.R;
import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.data.MyDocsDataModel;
import com.example.kashif.android.databinding.ActivityMyDocsBinding;
import com.example.kashif.android.ui.base.BaseActivity;
import com.example.kashif.android.ui.home.HomeActivity;
import com.example.kashif.android.ui.myDocsActivity.myDocsDetailActivity.MyDocsDetailActivity;
import com.example.kashif.android.utils.AlertDialogHelper;
import com.example.kashif.android.utils.CommonUtils;
import com.example.kashif.android.utils.Constants;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

import static com.example.kashif.android.utils.Constants.DELETE_SELECTED_FILES;
import static com.example.kashif.android.utils.Constants.MY_APPLICATION_DIR_NAME;

public final class MyDocsActivity extends BaseActivity implements MyDocsMvpView, MyDocsAdapter.MyDocsClickListener, View.OnDragListener, AlertDialogHelper.AlertDialogListener {

    @Inject
    DataManager mDataManager;
    @Inject
    MyDocsPresenter myDocsPresenter;
    @Inject
    MyDocsAdapter myDocsAdapter;
    private AlertDialogHelper alertDialogHelper;
    ActivityMyDocsBinding binding;
    private int newContactPosition = -1;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_docs);
        activityComponent().inject(this);
        binding.setListener(this);
        initilzeStuff();
    }

    private void initilzeStuff() {
        myDocsPresenter.attachView(this);
        binding.rvMyDocs.setAdapter(myDocsAdapter);
        myDocsAdapter.setClickListener(this);
        binding.pbMyDocs.setVisibility(View.VISIBLE);
        myDocsPresenter.getFiles(MyDocsActivity.this);
        binding.rvMyDocs.setOnDragListener(this);
        alertDialogHelper = new AlertDialogHelper(this, this);

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
        Intent home = new Intent(MyDocsActivity.this, HomeActivity.class);
        startActivity(home);
        finish();
    }

    public void showSearchView() {
        binding.searchBarLayout.setVisibility(View.VISIBLE);
    }

    public void hideSearchView() {
        binding.searchFiles.setText("");
        myDocsAdapter.getFilter().filter("");
        binding.searchBarLayout.setVisibility(View.GONE);
    }

    public void createFilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.create_folder);
        final View customLayout = getLayoutInflater().inflate(R.layout.create_folder_layout, null);
        builder.setView(customLayout);
        builder.setPositiveButton(R.string.create, (dialog, which) -> {
            EditText editText = customLayout.findViewById(R.id.editText);
            editText.setFocusable(true);
            myDocsPresenter.createFolder(editText.getText().toString().trim());
            dialog.dismiss();

        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            dialog.dismiss();

        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
            alertDialogHelper.showAlertDialog(R.string.delete, R.string.delete_all_files, R.string.yes, R.string.no, false, DELETE_SELECTED_FILES);
        } else {
            CommonUtils.showToast(MyDocsActivity.this, R.string.select_files_delete);
        }
    }

    @Override
    public void onPositiveClick(int operation) {
        if (operation == DELETE_SELECTED_FILES) {
            List<MyDocsDataModel> pdfFiles = myDocsAdapter.getSelectedFiles();
            myDocsPresenter.deleteSelectedFiles(pdfFiles);
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
        Intent home = new Intent(MyDocsActivity.this, HomeActivity.class);
        startActivity(home);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myDocsPresenter.detachView();
    }

    @Override
    public void onClick(View v) {

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
    public void onNewFolderCreateSuccess(String folderName) {
        myDocsAdapter.clearLists();
        myDocsPresenter.getFiles(MyDocsActivity.this);
    }

    @Override
    public void onDeleteFilesSuccess(String msg) {
        myDocsAdapter.clearLists();
        myDocsPresenter.getFiles(MyDocsActivity.this);
    }

    @Override
    public void onFolderNameClicked(MyDocsDataModel files) {
        Intent detail = new Intent(MyDocsActivity.this, MyDocsDetailActivity.class);
        detail.putExtra(Constants.NAME, files.getName());
        detail.putExtra(Constants.PATH, files.getPath());
        startActivity(detail);
    }

    @Override
    public void onDeleteFolderClicked(MyDocsDataModel files) {

        if (!files.isFile()) {
            File dir = new File(files.getPath());
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
        }

        myDocsAdapter.clearLists();
        myDocsPresenter.getFiles(MyDocsActivity.this);
    }

    @Override
    public void onDeleteFileClicked(MyDocsDataModel files) {
        File imageFile = new File(files.getPath());
        if (imageFile.exists()) {
            boolean isDeleted = imageFile.delete();
            Timber.d("Image File Deleted Successfully - > %s", isDeleted);
        }

        myDocsAdapter.clearLists();
        myDocsPresenter.getFiles(MyDocsActivity.this);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        View selectedView = (View) event.getLocalState();
        RecyclerView rcvSelected = (RecyclerView) v;

        try {
            currentPosition = binding.rvMyDocs.getChildAdapterPosition(selectedView);
            if (currentPosition != -1) {
                MyDocsDataModel myDocsDataModel = myDocsAdapter.getItemAtPosition(currentPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                Timber.e("ACTION_DRAG_STARTED");
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true;
                }
                return false;

            case DragEvent.ACTION_DRAG_LOCATION:
                Timber.e("ACTION_DRAG_LOCATION");
                View onTopOf = rcvSelected.findChildViewUnder(event.getX(), event.getY());
                assert onTopOf != null;
                newContactPosition = rcvSelected.getChildAdapterPosition(onTopOf);
                Timber.e("ACTION_DRAG_LOCATION" + newContactPosition);
                break;

            case DragEvent.ACTION_DROP:
                Timber.e("ACTION_DROP");
                ClipData.Item item = event.getClipData().getItemAt(0);
                String dragData = item.getText().toString();
                Toast.makeText(MyDocsActivity.this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();

                MyDocsDataModel myDocsDataModel = myDocsAdapter.getItemAtPosition(currentPosition);
                MyDocsDataModel myDocsDataModelTarget = myDocsAdapter.getItemAtPosition(newContactPosition);

                if (myDocsDataModel.isFile() && myDocsDataModelTarget.isFile()) {
                    CommonUtils.showToast(MyDocsActivity.this, R.string.add_valid_file);
                } else {
                    String docPath = myDocsDataModel.getPath();
                    String destFolderName = myDocsDataModelTarget.getName();

                    String newFilePath = docPath.replace(MY_APPLICATION_DIR_NAME, MY_APPLICATION_DIR_NAME + "/" + destFolderName);

                    File oldFile = new File(docPath);
                    File newFile = new File(newFilePath);
                    boolean moveFile = oldFile.renameTo(newFile);
                    if (moveFile) {
                        CommonUtils.showToast(MyDocsActivity.this, "File Moved to -> " + destFolderName);
                        myDocsAdapter.removeAt(currentPosition);
                        int numberOfFiles = Integer.valueOf(myDocsDataModelTarget.getNumberOfFiles());
                        numberOfFiles++;
                        myDocsDataModelTarget.setNumberOfFiles(String.valueOf(numberOfFiles));
                        myDocsAdapter.notifyDataSetChanged();
                    } else {
                        CommonUtils.showToast(MyDocsActivity.this, "File does not Moved to" + destFolderName);
                    }
                }

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                Timber.e("ACTION_DRAG_ENDED");
                return true;
            default:
                Timber.e("Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }
}
