package com.example.foodgasm.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodgasm.R
import com.example.foodgasm.databinding.ActivityShoppingBinding
import com.example.foodgasm.utils.Resource
import com.example.foodgasm.viewmodel.CartAndDetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

//@AndroidEntryPoint
//class ShoppingActivity : AppCompatActivity() {
//    private val binding by lazy {
//        ActivityShoppingBinding.inflate(layoutInflater)
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        val navigation=findNavController(R.id.navHostFragment)
//        binding.bottomNavigation.setupWithNavController(navigation)
//

private lateinit var analytics: FirebaseAnalytics
@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<CartAndDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        analytics = Firebase.analytics
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)


        val navigation = findNavController(R.id.navHostFragment)
        binding.bottomNavigation.setupWithNavController(navigation)

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        val count = it.data?.size ?: 0
                        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
                        bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_blue)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

}