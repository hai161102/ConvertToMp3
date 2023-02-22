package com.haiprj.converttomp3.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    public static boolean isPermissionGranted(Context context, String... permissions){

        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermission(Activity activity, Context context, int requestCode, OnPermissionResult onPermissionResult, String... permissions) {
        activity.requestPermissions(permissions, requestCode);
    }

    public interface OnPermissionResult{
        void onGranted();
        void onDenied(String... permissions);
    }
}