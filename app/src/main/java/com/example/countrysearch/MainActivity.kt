package com.example.countrysearch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.countrysearch.ui.BaseActivity
import com.example.countrysearch.ui.main.MainFragment
import com.example.countrysearch.ui.main.MainViewModel
import com.example.countrysearch.ui.retry.ConnectionRetry


class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private val pd by lazy {
        AppCompatDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.showProgress.observe(this, Observer {

            if (it) showProgress() else dismiss()
        })

        viewModel.connection.observe(this, Observer {
            val currentFragment =
                this.supportFragmentManager.findFragmentById(R.id.container)
            if (it) {
                if (savedInstanceState == null) {
                    if (currentFragment !is MainFragment) {
                        checkPermissionDetails()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, MainFragment.newInstance())
                            .commitNow()
                    }
                }
            } else {
                if (currentFragment !is MainFragment) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ConnectionRetry.newInstance())
                        .commitNow()
                }
            }
        })
        viewModel.checkConnectivity()

    }


    private fun dismiss() {
        if (pd.isShowing)
            pd.dismiss()
    }

    private fun showProgress() {
        pd.setCancelable(false)
        pd.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            viewModel.location.value = true
        }
    }
}