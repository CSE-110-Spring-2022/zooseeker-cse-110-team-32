package com.example.zooseeker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Utilities {
    public static void notifyIfOffTrack(ShortestPathActivity activity) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Off track! Replan?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("No", (dialog, id) ->{
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
