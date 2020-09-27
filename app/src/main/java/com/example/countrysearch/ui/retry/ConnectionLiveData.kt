package com.example.countrysearch.ui.retry

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData


class ConnectionLiveData(val context: Context) : LiveData<Boolean>() {
    override fun onActive() {
        super.onActive()
        context.registerReceiver(
            mNetworkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(mNetworkReceiver)
    }

    private val mNetworkReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.extras != null) {
                val activeNetwork =
                    intent.extras!![ConnectivityManager.EXTRA_NETWORK_INFO] as NetworkInfo?
                postValue(activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting)
            }
        }
    }
}