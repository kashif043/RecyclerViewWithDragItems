package com.example.kashif.android.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

import static com.example.kashif.android.utils.Constants.MY_APPLICATION_DIR_NAME;

public final class CommonUtils {

    public static void showToast(Context ctx, CharSequence msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context ctx, int resourceId) {
        Toast.makeText(ctx, resourceId, Toast.LENGTH_SHORT).show();
    }

    public static File getPathFromUri(Context context, final Uri argUri) {
        String strDocumentId = null;
        Cursor objCursor = context.getContentResolver().query(argUri, null, null, null, null);
        if (objCursor != null) {
            try {
                objCursor.moveToFirst();
                strDocumentId = objCursor.getString(0);
                strDocumentId = strDocumentId.substring(strDocumentId.lastIndexOf(":") + 1);
            } finally {
                objCursor.close();
            }
        }
        objCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{strDocumentId}, null);
        if (objCursor != null) {
            try {
                objCursor.moveToFirst();
                final String strDocument = objCursor.getString(objCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                return new File(strDocument);
            } finally {
                objCursor.close();
            }
        }
        return null;
    }

    public static String getMyDocsPath(Context context, String root) {
        File rootDir = context.getExternalFilesDir(root);
        File transmissionFolder = null;
        if (rootDir != null) {
            transmissionFolder = new File(rootDir.getAbsolutePath() + "/" + MY_APPLICATION_DIR_NAME);
        } else {
            transmissionFolder = new File(Environment.getExternalStorageDirectory(), MY_APPLICATION_DIR_NAME);
        }

        if (!transmissionFolder.exists())
            transmissionFolder.mkdir();

        return transmissionFolder.getAbsolutePath();
    }

}