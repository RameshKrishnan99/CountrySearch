package com.example.countrysearch.ui.main

import android.Manifest
import android.content.Intent
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countrysearch.R
import com.example.countrysearch.databinding.MainFragmentBinding
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.model.weather.WeatherDetailsItem
import com.example.countrysearch.ui.adapter.CountryAdapter
import com.example.countrysearch.ui.details.DetailActivity
import com.example.countrysearch.ui.details.DetailFragment
import com.example.countrysearch.util.ClickListener
import com.google.android.gms.location.*
import java.util.*


class MainFragment : Fragment(), ClickListener<Any> {

    companion object {
        fun newInstance() = MainFragment()
    }

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
        Log.e(TAG, ": ${object{}.javaClass.enclosingMethod?.name}")
        setHasOptionsMenu(true)
        bind = MainFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, ": ${object{}.javaClass.enclosingMethod?.name}")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, ": ${object{}.javaClass.enclosingMethod?.name}")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        bind.apply {
            toolbar.title = resources.getString(R.string.app_name)
            viewmodel = viewModel
            listener = this@MainFragment
            model = WeatherDetailsItem()
            rvCountryList.layoutManager = LinearLayoutManager(requireContext())
        }
        (requireActivity() as AppCompatActivity)?.let {
            it.setSupportActionBar(bind.toolbar)
        }
        val adapter = CountryAdapter(requireActivity() as ClickListener<Any>)
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
        })

        viewModel.location.observe(viewLifecycleOwner, Observer {
            if (it) {
                createLocationRequest()
                createLocationCallback()
                initLocationCallback()
            }
        })

    }

    private fun getWeather(lat: Double, long: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, long, 1)
        val cityName: String = addresses[0].locality
        Log.d("TAG", "cityName:$cityName ")
//        viewModel.getLocationKey(cityName)
    }

    override fun onClick(data: Any) {

        when (data) {
            is CountryResponseItem -> {
                /*startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra("country_details", data)
                })*/

            }
            is Int -> {
                bind.editText.text.clear()
                bind.searchConstraint.visibility = View.GONE
                bind.weather.visibility = View.VISIBLE
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            bind.searchConstraint.visibility = View.VISIBLE
            bind.weather.visibility = View.GONE
        }
        return super.onOptionsItemSelected(item)
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