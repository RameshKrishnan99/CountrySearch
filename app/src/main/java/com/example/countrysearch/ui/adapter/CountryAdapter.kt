package com.example.countrysearch.ui.adapter

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countrysearch.R
import com.example.countrysearch.databinding.AdapterCountryItemBinding
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.util.ClickListener
import com.example.countrysearch.util.Connection
import com.example.countrysearch.util.Util
import com.example.countrysearch.util.ViewHolder
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou


class CountryAdapter(
    val listener: ClickListener<Any>
) :
    RecyclerView.Adapter<ViewHolder<AdapterCountryItemBinding>>() {
    private val TAG: String = "CountryAdapter"
    private var data = ArrayList<CountryResponseItem>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<AdapterCountryItemBinding> {
        return ViewHolder(
            AdapterCountryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterCountryItemBinding>,
        position: Int
    ) {
        holder.binding.listener = listener
        holder.binding.model = data[position]

        holder.binding.ivFlag.apply {
            if (data[position].flag.isNullOrEmpty()) {
                setImageResource(R.drawable.ic_default_image)
            } else {
                val url = data[position].flag
                Log.d(TAG, "url: $url")
                GlideToVectorYou.justLoadImage(context as Activity, Uri.parse(url), this, R.drawable.ic_default_image)

            }

        }
    }

    fun setData(list: MutableList<CountryResponseItem>?) {

        list?.let {
            data = list as ArrayList<CountryResponseItem>
        }
        notifyDataSetChanged()
    }



}