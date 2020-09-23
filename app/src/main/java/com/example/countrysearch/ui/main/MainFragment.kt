package com.example.countrysearch.ui.main

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countrysearch.R
import com.example.countrysearch.databinding.MainFragmentBinding
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.ui.adapter.CountryAdapter
import com.example.countrysearch.ui.details.DetailActivity
import com.example.countrysearch.util.ClickListener
import java.util.*

class MainFragment : Fragment(), ClickListener<Any> {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var bind: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        bind = MainFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        bind.viewmodel = viewModel
        bind.listener = this
        bind.rvCountryList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CountryAdapter(this@MainFragment)
        bind.rvCountryList.adapter = adapter
        var dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        bind.rvCountryList.addItemDecoration(dividerItemDecoration)
        viewModel.getCountryList().observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
        viewModel.searchData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
        viewModel.editTextContent.observe(viewLifecycleOwner, Observer {
            viewModel.searchData(it)
        })
        viewModel.weatherData.observe(viewLifecycleOwner, Observer {

            bind.model = it
//            bind.invalidateAll()
        })

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(13.135991, 80.288951, 1)
        val cityName: String = addresses[0].locality
//        val stateName: String = addresses[0].getAddressLine(1)
//        val countryName: String = addresses[0].getAddressLine(2)
        Log.d("TAG", "cityName:$cityName ")
        viewModel.getLocationKey(cityName)
    }

    override fun onClick(data: Any) {

        when (data) {
            is CountryResponseItem -> {
                startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra("country_details", data)
                })
            }
            is Int -> {
                bind.editText.text.clear()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            bind.appBarLayout.visibility = View.VISIBLE
            bind.weather.weatherItem.visibility = View.GONE
        }
        return super.onOptionsItemSelected(item)
    }
}