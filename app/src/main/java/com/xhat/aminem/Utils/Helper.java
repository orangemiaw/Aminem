package com.xhat.aminem.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.xhat.aminem.R;

import java.util.Timer;
import java.util.TimerTask;

public class Helper {
    public static final Integer STATUS_ENABLE = 1;
    public static final Integer STATUS_DISABLE = 2;
    static SessionManager sessionManager;

    public static void clearSession(final Context context) {
        sessionManager = new SessionManager(context);

        sessionManager.saveSPString(SessionManager.SP_USER_ID, "spUserId");
        sessionManager.saveSPString(SessionManager.SP_TOKEN, "spToken");
        sessionManager.saveSPBoolean(SessionManager.SP_SUDAH_LOGIN, false);
        sessionManager.saveSPString(SessionManager.SP_IMAGE_SLIDER, "");
    }

    public static void showAlertDialog(final Context context, final String title, final String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle(title);

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage(message)
                .setIcon(R.drawable.ic_info)
                .setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        final Timer alertTimer = new Timer();
        alertTimer.schedule(new TimerTask() {
            public void run() {
                alertDialog.dismiss();
                // this will cancel the timer of the system
                alertTimer.cancel();
            }
        }, 3000); // the timer will count 2 seconds....
    }

    public static void showTimeOut(final Context context) {
        Helper.showAlertDialog(context,"Error", "Sorry, connection timeout.");
    }

    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
