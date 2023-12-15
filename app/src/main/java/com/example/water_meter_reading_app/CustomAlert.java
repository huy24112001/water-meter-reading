package com.example.water_meter_reading_app;

import static com.google.android.gms.tasks.Tasks.await;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.concurrent.CountDownLatch;

public class CustomAlert {

    // Hàm hiển thị AlertDialog đơn giản
    public static boolean showAlertQuestion(Context context, String title, String message) {
        final boolean[] rs = {false};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       rs[0] = true;

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        rs[0] = false;

                    }
                })
                .show();



        return  rs[0];
    }

     public static void showAlert(Context context, String title, String message) {

         AlertDialog.Builder builder = new AlertDialog.Builder(context);
         builder.setTitle(title)
                 .setMessage(message)
                 .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {

                     }
                 })

                 .show();


     }
}
