package com.caunb163.mxh.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.caunb163.mxh.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView: BottomNavigationView = view.findViewById(R.id.bottom_navigation_view)

        val navHostFragment: NavHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_home_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(navView, navController)
    }

}