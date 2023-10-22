package com.example.foodgasm.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodgasm.R
import com.example.foodgasm.databinding.ActivityShoppingBinding

class ShoppingActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navigation=findNavController(R.id.navHostFragment)
        binding.bottomNavigation.setupWithNavController(navigation)
    }
}