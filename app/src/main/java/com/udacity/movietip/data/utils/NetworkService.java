package com.udacity.movietip.data.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.udacity.movietip.data.utils.NetWorkReceiver.NetWorkReceiverListener;
// jobservice requires min sdk 21. that leaves 10% of KitKat users behind: https://developer.android.com/about/dashboards/
public class NetworkService extends JobService implements NetWorkReceiver.NetWorkReceiverListener{

    private static NetWorkReceiver mNetworkReceiver;
    public static final String NETWORK_STATUS = "isConnected";

    @Override
    public void onCreate() {
        super.onCreate();
        mNetworkReceiver = new NetWorkReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        // only remain running while processing any commands sent here
        // https://developer.android.com/reference/android/app/Service#START_NOT_STICKY
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        unregisterReceiver(mNetworkReceiver);
        return true;
    }

    @Override
    public void onNetworkChanged(boolean isConnected) {
        // instead of string message use LocalBroadcast to let my app know there's a connection
        String message = isConnected ? "Yep, device has a network connection" : "Oops! No network connection!";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);

    }
}
