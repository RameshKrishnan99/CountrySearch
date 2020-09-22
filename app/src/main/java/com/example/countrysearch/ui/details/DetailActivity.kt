package com.example.countrysearch.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.countrysearch.R
import com.example.countrysearch.databinding.ActivityDetailBinding
import com.example.countrysearch.model.CountryResponseItem

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = DataBindingUtil.setContentView<ActivityDetailBinding>(
            this,
            R.layout.activity_detail
        ).apply {
            this.setLifecycleOwner(this@DetailActivity)
            this.model = intent.getSerializableExtra("country_details") as CountryResponseItem
        }
        Glide.with(this)
            .load(binding.model?.flag)
            .fitCenter()
            .placeholder(R.drawable.ic_default_image)
            .into(binding.toolbarImage)
    }
}