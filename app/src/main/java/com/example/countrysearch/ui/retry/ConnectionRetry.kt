package com.example.countrysearch.ui.retry

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.countrysearch.databinding.InternetNotAvailableBinding
import com.example.countrysearch.receivers.NetworkChangeReceiver
import com.example.countrysearch.ui.main.MainViewModel


class ConnectionRetry : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var bind: InternetNotAvailableBinding

    private var mNetworkReceiver: BroadcastReceiver? = null
    companion object {
        fun newInstance() = ConnectionRetry()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    override fun onStop() {
        super.onStop()
        try {
            requireActivity().unregisterReceiver(mNetworkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = InternetNotAvailableBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        bind.viewmodel = viewModel
        mNetworkReceiver = NetworkChangeReceiver(viewModel)
    }
}