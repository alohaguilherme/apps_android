package com.example.guilherme.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean validPermissions(String[] permissions, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23 ){

            List<String> listPermissions = new ArrayList<>();

            for (String permission : permissions){
                Boolean checkPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                 if ( !checkPermission ) listPermissions.add(permission);
            }

            if ( listPermissions.isEmpty() ) return true;
            String [] newPermissions = new String[listPermissions.size()];
            listPermissions.toArray( newPermissions);

            //solicita permissão
            ActivityCompat.requestPermissions(activity, newPermissions, requestCode);


        }

        return true;
    }
}
