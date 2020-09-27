package com.example.countrysearch.ui.details

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.countrysearch.R
import com.example.countrysearch.databinding.FragmentDetailBinding
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.ui.main.MainViewModel



class DetailFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var bind: FragmentDetailBinding

    private var data: CountryResponseItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getSerializable(ARG_PARAM1) as CountryResponseItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentDetailBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        bind.model = data
        bind.toolbar.apply {
            title = bind.model?.name
        }
        (requireActivity() as AppCompatActivity)?.let {
            it.setSupportActionBar(bind.toolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        bind.toolbar.setNavigationOnClickListener(View.OnClickListener {
//            fragmentManager?.popBackStack()
//            Navigation.findNavController(it).popBackStack()
            findNavController().popBackStack()
        })
        Glide.with(requireActivity())
            .load(bind.model?.flag)
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
                                    bind.collapseToolbar.setStatusBarScrimColor(dominant)
                                    bind.collapseToolbar.setContentScrimColor(dominant)
                                    bind.collapseToolbar.setBackgroundColor(dominant)
                                }
                            })
                        }
                    }
                    return false
                }

            })
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_cloud_offline)
            .into(bind.toolbarImage)
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

    companion object {
        fun newInstance() = DetailFragment()
        const val ARG_PARAM1 = "country_details"
    }
}