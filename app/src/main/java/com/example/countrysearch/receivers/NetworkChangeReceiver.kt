package com.example.countrysearch.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.countrysearch.ui.main.MainViewModel
import com.example.countrysearch.util.Connection

class NetworkChangeReceiver(val viewModel: MainViewModel) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && Connection.isOnline(context)) {
            viewModel.connection.value = true
        }
    }
}