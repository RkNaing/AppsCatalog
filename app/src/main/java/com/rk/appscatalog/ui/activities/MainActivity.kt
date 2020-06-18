package com.rk.appscatalog.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rk.appscatalog.R
import com.rk.appscatalog.ui.fragments.AppsListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentListFragment =
            supportFragmentManager.findFragmentById(R.id.main_listFragmentContainer)
        if (currentListFragment == null) {
            val appsListFragment = AppsListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.main_listFragmentContainer, appsListFragment)
                .commit()
        }
    }
}