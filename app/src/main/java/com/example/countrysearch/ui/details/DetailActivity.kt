package com.example.countrysearch.ui.details

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
        binding.toolbar.apply {
            title = binding.model?.name
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        Glide.with(this)
            .load(binding.model?.flag)
            .fitCenter()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let {
                        val bitmap = drawableToBitmap(it)
                        bitmap?.let {
                            Palette.from(bitmap).generate({ palette ->
                                if (palette != null) {
                                    val dominant: Int =
                                        palette.getDominantColor(0x000000) // <=== color you want
                                    binding.collapseToolbar.setStatusBarScrimColor(dominant)
                                    binding.collapseToolbar.setContentScrimColor(dominant)
                                    binding.collapseToolbar.setBackgroundColor(dominant)
                                }
                            })
                        }
                    }
                    return false
                }

            })
            .placeholder(R.drawable.ic_default_image)
            .into(binding.toolbarImage)
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return (drawable as BitmapDrawable).getBitmap()
        }
        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}