package com.caunb163.mxh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(applicationContext)
        listenBackStackChange()
    }

    private fun listenBackStackChange() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)

        val navHostChildFragmentManager = navHostFragment?.childFragmentManager

        navHostChildFragmentManager?.addOnBackStackChangedListener {

            val backStackEntryCount = navHostChildFragmentManager.backStackEntryCount
            val fragments = navHostChildFragmentManager.fragments

            println("😛 NavHost count: $backStackEntryCount, fragments: $fragments")

        }
    }
}