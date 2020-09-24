package com.example.countrysearch.util

import java.text.SimpleDateFormat
import java.util.*

object Util {
    fun getCurrentDate(): String? {
        val sdf = SimpleDateFormat("EEE MMM dd")
        return sdf.format(Date())
    }
    fun getCurrentTime(): String? {
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(Date())
    }
}