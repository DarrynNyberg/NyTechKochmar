package com.info.post.postinfo.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionDetector {
    public static boolean isConnection(Context context) {
        ConnectivityManager cm =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}