package com.udacity.movietip.data.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
// Reference: explained https://medium.com/google-developers/scheduling-jobs-like-a-pro-with-jobscheduler-286ef8510129
// Reference: http://www.singhajit.com/notify-android-device-network-status-changes/
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
