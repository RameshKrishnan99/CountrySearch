package com.example.countrysearch.ui.retry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.countrysearch.databinding.InternetNotAvailableBinding
import com.example.countrysearch.ui.main.MainViewModel

class ConnectionRetry : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var bind: InternetNotAvailableBinding
    private val pd by lazy {
        AppCompatDialog(requireContext())
    }
    companion object {
        fun newInstance() = ConnectionRetry()
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
    }
}