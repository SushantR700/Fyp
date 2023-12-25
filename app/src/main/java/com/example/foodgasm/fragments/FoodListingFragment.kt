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
import com.example.foodgasm.databinding.FragmentFoodListingBinding
import com.example.foodgasm.utils.Resource
import com.example.foodgasm.utils.hideBottomNavigationView
import com.example.foodgasm.viewmodel.DetailsViewModel
import com.example.foodgasm.viewmodel.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodListingFragment : Fragment(R.layout.fragment_food_listing) {
    private lateinit var binding: FragmentFoodListingBinding
    private val binding1 by lazy {
        ChildItemBinding.inflate(layoutInflater)
    }
    private lateinit var foodListingAdapter: FoodListingAdapter
    private val args by navArgs<FoodListingFragmentArgs>()
    private lateinit var item:RestaurantModel
    private lateinit var owner:RestaurantModel
    private lateinit var restaurantId:String
    private val viewModel2 by viewModels<DetailsViewModel>()


    private val viewModel by viewModels<MenuViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = FragmentFoodListingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        owner=args.owner
        item = args.item
        restaurantId = args.restaurant
        viewModel.fetchFood(item.id,restaurantId)

        setUpRv()
        foodListingAdapter.onClick = { item ->
            val b=Bundle().apply { putParcelable("item2",item)
                putParcelable("owner",owner)
            }

            val actionId = R.id.action_foodListingFragment_to_foodDetailsFragment
            Log.d("Navigation", "Action ID: $actionId")
            findNavController().navigate(actionId, b)
        }

        lifecycleScope.launch {
            viewModel.food.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        foodListingAdapter.differ2.submitList(result.data)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), item.id, Toast.LENGTH_SHORT).show()

                    }
                    else -> Unit
                }
            }
        }

    }

    private fun setUpRv() {
        foodListingAdapter = FoodListingAdapter()
        binding.foodrv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = foodListingAdapter
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