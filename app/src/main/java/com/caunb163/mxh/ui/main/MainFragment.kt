package com.caunb163.mxh.ui.main

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject

@InternalCoroutinesApi
class MainFragment : BaseFragment(R.layout.fragment_main) {
    private val TAG = "MainFragment"

    private val viewModel: MainViewModel by inject()
    private val localStorage: LocalStorage by inject()

    @SuppressLint("RestrictedApi")
    override fun initView(view: View) {
        val navView: BottomNavigationView = view.findViewById(R.id.bottom_navigation_view)

        val navHostFragment: NavHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_home_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        NavigationUI.setupWithNavController(navView, navController)

//        val navHostFragment: NavHostFragment =
//            childFragmentManager.findFragmentById(R.id.nav_home_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        val navigator = KeepStateNavigator(
//            requireContext(),
//            navHostFragment.childFragmentManager,
//            R.id.nav_home_fragment
//        )
////
//        navController.navigatorProvider.addNavigator(navigator)
//        navController.setGraph(R.navigation.home_nav_graph)
//
//        navView.setupWithNavController(navController)

    }

    override fun initListener() {}

    override fun initObserve() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as User)
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {}

    private fun onSuccess(user: User) {
        localStorage.saveAccount(user)
    }

    private fun onFailure(message: String) {
        showToast(message)
    }

}