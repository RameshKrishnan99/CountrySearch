package com.example.countrysearch

import android.app.AlertDialog
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.countrysearch.ui.main.MainFragment
import com.example.countrysearch.util.Connection
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (Connection.isOnline(this)) {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
            }
        } else {
            showNoInternetDialog()
        }


    }

    private fun showNoInternetDialog() {
        try {
            val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Info")
            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again")
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
            alertDialog.setButton("OK",
                DialogInterface.OnClickListener { dialog, which -> finish() })
            alertDialog.show()
        } catch (e: Exception) {
        }
    }

}