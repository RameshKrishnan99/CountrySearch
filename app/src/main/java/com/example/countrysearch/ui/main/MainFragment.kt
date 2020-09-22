package com.example.countrysearch.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countrysearch.databinding.MainFragmentBinding
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.ui.adapter.CountryAdapter
import com.example.countrysearch.ui.details.DetailActivity
import com.example.countrysearch.util.ClickListener

class MainFragment : Fragment(), ClickListener<CountryResponseItem> {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var bind: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bind = MainFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        bind.viewmodel = viewModel
        bind.rvCountryList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CountryAdapter(requireContext(),this@MainFragment)
        bind.rvCountryList.adapter = adapter
        viewModel.getCountryList().observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
        var dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        bind.rvCountryList.addItemDecoration(dividerItemDecoration)

        viewModel.editTextContent.observe(viewLifecycleOwner, Observer {
            adapter.searchData(it)
        })
    }

    override fun onClick(model: CountryResponseItem) {
        startActivity(Intent(requireContext(),DetailActivity::class.java).apply {
            putExtra("country_details",model)
        })
    }

}