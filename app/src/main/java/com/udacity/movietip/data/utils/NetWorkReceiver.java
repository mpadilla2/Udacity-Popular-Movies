package com.udacity.movietip.data.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
// jobscheduler explained https://medium.com/google-developers/scheduling-jobs-like-a-pro-with-jobscheduler-286ef8510129
// use jobservice for broadcast receiver https://stackoverflow.com/a/48666854 or https://github.com/jiteshmohite/Android-Network-Connectivity
// then use localbroadcastmanager to let app know it's connected
// http://www.singhajit.com/notify-android-device-network-status-changes/

// use jobscheduler to check for internet and only download from internet when there is a connection
// can also work with changes to a content provider
public class NetWorkReceiver extends BroadcastReceiver {

    private NetWorkReceiverListener mNetWorkReceiverListener;

    NetWorkReceiver(NetWorkReceiverListener listener){
        mNetWorkReceiverListener = listener;
    }

    public interface NetWorkReceiverListener {
        void onNetworkChanged(boolean isConnected);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mNetWorkReceiverListener.onNetworkChanged(isConnected(context));
    }

    public static boolean isConnected(Context context){

        Boolean mConnected = false;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
                mConnected = true;
            } else {
                mConnected = false;
            }
        }

        return mConnected;
    }
}
