package com.example.foodgasm.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodgasm.R
import com.example.foodgasm.adapters.FoodListingAdapter
import com.example.foodgasm.data.CartProduct
import com.example.foodgasm.data.RestaurantModel
import com.example.foodgasm.databinding.ChildItemBinding
import com.example.foodgasm.databinding.FragmentFoodDetailsBinding
import com.example.foodgasm.databinding.FragmentFoodListingBinding
import com.example.foodgasm.utils.Resource
import com.example.foodgasm.utils.hideBottomNavigationView
import com.example.foodgasm.viewmodel.DetailsViewModel
import com.example.foodgasm.viewmodel.MenuViewModel
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodDetailsFragment : Fragment(R.layout.fragment_food_details) {
    private lateinit var binding: FragmentFoodDetailsBinding
    private val args by navArgs<FoodDetailsFragmentArgs>()
    private lateinit var item2:RestaurantModel
    private lateinit var owner:RestaurantModel
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = FragmentFoodDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item2 = args.item2
        owner = args.owner
        binding.register.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(item2,1))
        }


        lifecycleScope.launch {
            viewModel.addToCart.collectLatest { result ->
                when (result) {
                    is Resource.Loading ->{
                        binding.register.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.register.revertAnimation()
                        Toast.makeText(requireContext(), owner.id, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Failed to add", Toast.LENGTH_SHORT).show()

                    }
                    else -> Unit
                }
            }
        }


    }
    override fun onResume() {
        super.onResume()
        // Add back button listener
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Navigate up when back button is pressed
            findNavController().navigateUp()
        }
    }
}