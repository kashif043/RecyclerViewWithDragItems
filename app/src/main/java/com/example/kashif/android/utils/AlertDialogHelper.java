package com.example.kashif.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogHelper {
    Context context;
    AlertDialog alertDialog = null;
    AlertDialogListener callBack;
    Activity current_activity;

    public AlertDialogHelper(Context context, AlertDialogListener listener) {
        this.context = context;
        this.current_activity = (Activity) context;
        this.callBack = listener;
    }

    public void showAlertDialog(int title, int message, int positive, int negative, boolean isCancelable, int operation) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(current_activity);

        if (!TextUtils.isEmpty(String.valueOf(title)))
            alertDialogBuilder.setTitle(title);
        if (!TextUtils.isEmpty(String.valueOf(message)))
            alertDialogBuilder.setMessage(message);

        if (!TextUtils.isEmpty(String.valueOf(positive))) {
            alertDialogBuilder.setPositiveButton(positive,
                    (arg0, arg1) -> {
                        callBack.onPositiveClick(operation);
                        alertDialog.dismiss();
                    });
        }
        if (!TextUtils.isEmpty(String.valueOf(negative))) {
            alertDialogBuilder.setNegativeButton(negative,
                    (arg0, arg1) -> {
                        alertDialog.dismiss();
                    });
        } else {
            try {
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        Button negative_button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        negative_button.setVisibility(View.GONE);

                        Looper.loop();

                    }
                }.start();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        alertDialogBuilder.setCancelable(isCancelable);


        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showAlertDialog1(int title, int message, int positive, int negative, boolean isCancelable, int operation, AlertDialogListener listener) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(current_activity);

        if (!TextUtils.isEmpty(String.valueOf(title)))
            alertDialogBuilder.setTitle(title);
        if (!TextUtils.isEmpty(String.valueOf(message)))
            alertDialogBuilder.setMessage(message);

        if (!TextUtils.isEmpty(String.valueOf(positive))) {
            alertDialogBuilder.setPositiveButton(positive,
                    (arg0, arg1) -> {
                        listener.onPositiveClick(operation);
                        alertDialog.dismiss();
                    });
        }
        if (!TextUtils.isEmpty(String.valueOf(negative))) {
            alertDialogBuilder.setNegativeButton(negative,
                    (arg0, arg1) -> {
                        alertDialog.dismiss();
                    });
        } else {
            try {
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        Button negative_button = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        negative_button.setVisibility(View.GONE);

                        Looper.loop();

                    }
                }.start();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        alertDialogBuilder.setCancelable(isCancelable);


        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public interface AlertDialogListener {
        void onPositiveClick(int operation);
    }

}

