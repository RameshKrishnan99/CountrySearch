package com.example.countrysearch.ui.retry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.countrysearch.R
import com.example.countrysearch.databinding.InternetNotAvailableBinding
import com.example.countrysearch.ui.details.DetailFragment
import com.example.countrysearch.ui.main.MainViewModel


class ConnectionRetry : Fragment() {
    private lateinit var bind: InternetNotAvailableBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = InternetNotAvailableBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val connectionLiveData = ConnectionLiveData(requireContext())
        connectionLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController()
                    .navigate(R.id.action_connectionRetry_to_mainFragment)
            }
        })
    }


}