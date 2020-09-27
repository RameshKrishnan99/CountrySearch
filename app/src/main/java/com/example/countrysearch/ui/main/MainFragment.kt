package com.example.countrysearch.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.countrysearch.R
import com.example.countrysearch.databinding.MainFragmentBinding
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.model.weather.WeatherDetailsItem
import com.example.countrysearch.ui.adapter.StaggeredAdapter
import com.example.countrysearch.ui.details.DetailFragment
import com.example.countrysearch.util.ClickListener
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.weather_item.view.*
import java.util.*


class MainFragment : Fragment(), ClickListener<Any> {

    private lateinit var adapter: StaggeredAdapter
    private val TAG = MainFragment::class.qualifiedName
    private lateinit var bind: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var mLocationCallback: LocationCallback
    lateinit var mLocationRequest: LocationRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e(TAG, ": ${object {}.javaClass.enclosingMethod?.name}")
        setHasOptionsMenu(true)
        bind = MainFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        bind.apply {
            toolbar.title = resources.getString(R.string.app_name)
            viewmodel = viewModel
            listener = this@MainFragment
            model = WeatherDetailsItem()
            rvCountryList.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        (requireActivity() as AppCompatActivity)?.let {
            it.setSupportActionBar(bind.toolbar)
        }
        adapter = StaggeredAdapter(this@MainFragment)
        bind.rvCountryList.adapter = adapter

        viewModel.countryData.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty())
                showServerError()
            else
                showList(it)

        })
        if (viewModel.countryData.value.isNullOrEmpty())
            viewModel.getCountryList()

        if(!viewModel.editTextContent.value.isNullOrEmpty())
            showSearch()

        viewModel.searchData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
        viewModel.editTextContent.observe(viewLifecycleOwner, Observer {
            viewModel.searchData(it)
        })
        viewModel.weatherData.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                bind.weather.tv_noData.visibility = View.VISIBLE
                bind.weather.progress.visibility = View.GONE
            } else
                bind.model = it
        })

        viewModel.location.observe(viewLifecycleOwner, Observer {
            if (it) {
                createLocationRequest()
                createLocationCallback()
                initLocationCallback()
            }
        })

    }

    private fun showList(it: MutableList<CountryResponseItem>) {
        adapter.setData(it)
        rv_countryList.visibility = View.VISIBLE
        bind.clServerError.visibility = View.GONE
    }

    private fun showServerError() {
        bind.clServerError.visibility = View.VISIBLE
        bind.rvCountryList.visibility = View.GONE
    }

    private fun getWeather(lat: Double, long: Double) {
        requireContext()?.let {
            val geocoder = Geocoder(it, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(lat, long, 1)
            val cityName: String = addresses[0].locality
            Log.d("TAG", "cityName:$cityName ")
            viewModel.getLocationKey(cityName)
        }

    }

    override fun onClick(data: Any) {

        when (data) {
            is CountryResponseItem -> {
                findNavController()
                    .navigate(R.id.action_mainFragment_to_detailFragment, Bundle().apply {
                        putSerializable(DetailFragment.ARG_PARAM1, data)
                    })

            }
            is Int -> {
                hideSearch()
            }
        }

    }

    private fun hideSearch() {
        bind.editText.text.clear()
        bind.searchConstraint.visibility = View.GONE
        bind.weather.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search && !viewModel.countryData.value.isNullOrEmpty()) {
            showSearch()
        } else {
            showError(resources.getString(R.string.data_not_available))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showError(errorMsg: String) {
        val snackbar = Snackbar
            .make(bind.root, errorMsg, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun showSearch() {
        bind.searchConstraint.visibility = View.VISIBLE
        bind.weather.visibility = View.GONE
    }

    fun createLocationRequest(): LocationRequest {
        mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        return mLocationRequest
    }

    fun initLocationCallback() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        }

    }

    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                getWeather(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude
                )
            }
        }
    }

}