package com.one8.ioctest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static boolean isNetWorkConnected(Context context){
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity.getActiveNetworkInfo() != null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            boolean isAvailable = info.isAvailable();
            boolean isConnected = info.isConnected();

            return isAvailable && isConnected;
        }
        return false;
    }
}
