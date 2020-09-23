package com.example.countrysearch.util

import android.content.Context
import android.net.ConnectivityManager


object Connection {


    fun isOnline(context: Context): Boolean {
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        if (netInfo == null || !netInfo.isConnected || !netInfo.isAvailable) {
            return false
        }
        return true
    }
}