package com.example.countrysearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.countrysearch.model.CountryResponseItem
import com.example.countrysearch.ui.main.MainFragment
import com.example.countrysearch.util.ClickListener

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }


}