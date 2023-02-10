package com.haiprj.converttomp3.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

public class PermissionUtil {

    public static boolean isPermissionGranted(Context context, String... permissions){

        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermission(Activity activity, int requestCode, String... permissions) {

        activity.requestPermissions(permissions, requestCode);
    }
}