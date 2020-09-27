package com.example.countrysearch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.countrysearch.ui.BaseActivity
import com.example.countrysearch.ui.details.DetailFragment
import com.example.countrysearch.ui.main.MainFragment
import com.example.countrysearch.ui.main.MainViewModel
import com.example.countrysearch.util.Connection
import com.google.android.material.snackbar.Snackbar


class MainActivity : BaseActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private var doubleBackToExitPressedOnce: Boolean = false
    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel
    private val pd by lazy {
        AppCompatDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_graph)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.showProgress.observe(this, Observer {

            if (it) showProgress() else dismiss()
        })

        if (Connection.isOnline(this))
            graph.startDestination = R.id.mainFragment
        else
            graph.startDestination = R.id.connectionRetry

        navController.graph = graph
        checkPermissionDetails()
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

    override fun onDestroy() {
        super.onDestroy()
        dismiss()
    }

    override fun onBackPressed() {
        if (getForegroundFragment() is DetailFragment) {
            super.onBackPressed()
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            val snackbar = Snackbar
                .make(findViewById(android.R.id.content), resources.getString(R.string.doubleTap), Snackbar.LENGTH_LONG)
            snackbar.show()
            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }

    }

    private fun getForegroundFragment(): Fragment? {
        return navHostFragment?.getChildFragmentManager()?.getFragments()?.get(0)
    }
}