package com.example.kashif.android.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.example.kashif.android.R;
import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.databinding.ActivityHomeBinding;
import com.example.kashif.android.ui.base.BaseActivity;
import com.example.kashif.android.ui.myDocsActivity.MyDocsActivity;
import com.example.kashif.android.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

import static com.example.kashif.android.utils.Constants.IMAGE_EXTENSION;
import static com.example.kashif.android.utils.Constants.IMAGE_POSTFIX;
import static com.example.kashif.android.utils.Constants.PERMISSION_GALLERY;
import static com.example.kashif.android.utils.Constants.PERMISSION_LISTING;

public class HomeActivity extends BaseActivity implements HomeMvpView {

    @Inject
    DataManager mDataManager;
    @Inject
    HomePresenter homePresenter;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        activityComponent().inject(this);
        homePresenter.attachView(this);
        binding.setMyListener(this);
    }

    public void goToGallery() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_GALLERY);
        } else {
            onLaunchGallerySelection();
        }
    }

    public void goToMyDocs() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_LISTING);
        } else {
            gotToListing();
        }
    }

    public void onLaunchGallerySelection() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
    }

    public void gotToListing() {
        Intent myDocs = new Intent(HomeActivity.this, MyDocsActivity.class);
        startActivity(myDocs);
        finish();
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
    }

    @Override
    protected void onDestroy() {
        homePresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(final int iRequestCode, int iResultCode, final Intent argResultData) {
        super.onActivityResult(iRequestCode, iResultCode, argResultData);
        if (iRequestCode == 101) {
            if (iResultCode == Activity.RESULT_OK) {
                File objSelectedImage = CommonUtils.getPathFromUri(HomeActivity.this, argResultData.getData());
                if (objSelectedImage != null) {
                    String filename = String.format("%s-%s%s", System.currentTimeMillis(), IMAGE_POSTFIX, IMAGE_EXTENSION);
                    String destPath = mDataManager.getSharedPrefHelper().getMyFolderPath() + "/" + filename;
                    String currentPath = objSelectedImage.getAbsolutePath();
                    File oldFile = new File(currentPath);
                    File newFile = new File(destPath);

                    FileChannel source = null;
                    FileChannel destination = null;

                    try {
                        source = new FileInputStream(oldFile).getChannel();
                        destination = new FileOutputStream(newFile).getChannel();
                        destination.transferFrom(source, 0, source.size());
                        source.close();
                        destination.close();
                        Toast.makeText(this, R.string.file_upload_success, Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        Toast.makeText(this, R.string.file_upload_failure, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(final int iRequestCode, @NotNull final String xstrPermissions[], @NotNull final int[] xiGrantResults) {
        // Check the result of the permissions dialog
        boolean bAllPermissionsGranted = true;
        if (iRequestCode == PERMISSION_GALLERY) {
            for (int iPermission : xiGrantResults) {
                if (iPermission != PackageManager.PERMISSION_GRANTED) {
                    bAllPermissionsGranted = false;
                    break;
                }
            }
        } else {
            for (int iPermission : xiGrantResults) {
                if (iPermission != PackageManager.PERMISSION_GRANTED) {
                    bAllPermissionsGranted = false;
                    break;
                }
            }
        }

        // If we have all the needed permissions, trigger directly the capture button handler
        if (bAllPermissionsGranted) {
            if (iRequestCode == PERMISSION_GALLERY) {
                onLaunchGallerySelection();
            } else {
                gotToListing();
            }
        }
        // Otherwise, display an alert dialog indicating that the sample needs those permissions
        else {
            final AlertDialog.Builder objAlertBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.PopupTheme);
            objAlertBuilder.setTitle(R.string.about_permission)
                    .setMessage(R.string.dokmee_permission)
                    .setPositiveButton(R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

}
