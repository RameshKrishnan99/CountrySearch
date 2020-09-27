package com.example.countrysearch.ui.adapter

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.countrysearch.R
import com.example.countrysearch.databinding.AdapterStaggerredBinding
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.util.ClickListener
import com.example.countrysearch.util.ViewHolder
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

class StaggeredAdapter(
    val listener: ClickListener<Any>
) :
    RecyclerView.Adapter<ViewHolder<AdapterStaggerredBinding>>() {
    private val TAG: String = "CountryAdapter"
    private var data = ArrayList<CountryResponseItem>()
    private val set = ConstraintSet()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<AdapterStaggerredBinding> {
        return ViewHolder(
            AdapterStaggerredBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterStaggerredBinding>,
        position: Int
    ) {
        Log.e(TAG, "${object {}.javaClass.enclosingMethod?.name}: Pos = $position")
        holder.binding.listener = listener
        holder.binding.model = data[position]

        holder.binding.ivFlag.apply {
            if (data[position].flag.isNullOrEmpty()) {
                setImageResource(R.drawable.ic_default_image)
            } else {
                val url = data[position].flag
                Log.d(TAG, "url: $url")
                GlideToVectorYou.justLoadImage(
                    context as Activity,
                    Uri.parse(url),
                    this,
                    R.drawable.ic_loading,
                    R.drawable.ic_cloud_offline
                )

            }
            var ratio:String
            if (position % 2 == 0)
                ratio = "1:1"
            else
                ratio = "16:9"
            set.clone(holder.binding.parentContsraint)
            set.setDimensionRatio(this.id, ratio)
            set.applyTo(holder.binding.parentContsraint)
        }

    }

    fun setData(list: MutableList<CountryResponseItem>?) {

        list?.let {
            data = list as ArrayList<CountryResponseItem>
        }
        notifyDataSetChanged()
    }


}