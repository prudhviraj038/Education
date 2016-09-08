package com.techbox.education;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public void showAlertDialog(final Context context, String title, String message,
                                final Boolean status, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if(status != null) {
            // Setting alert dialog icon
          //  alertDialog.setIcon((status) ? R.drawable.sample : R.drawable.delete);

            // Setting OK Button
            if (status) {
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

            } else {
                alertDialog.setButton("OK", onClickListener);

            }
        }

                // Showing Alert Message
        alertDialog.show();
    }
}