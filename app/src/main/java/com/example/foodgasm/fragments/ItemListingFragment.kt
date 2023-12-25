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
import com.example.foodgasm.adapters.ItemListingAdapter
import com.example.foodgasm.data.RestaurantModel
import com.example.foodgasm.databinding.FragmentItemListingBinding
import com.example.foodgasm.databinding.ParentItemBinding
import com.example.foodgasm.utils.Resource
import com.example.foodgasm.utils.hideBottomNavigationView
import com.example.foodgasm.viewmodel.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemListingFragment : Fragment(R.layout.fragment_item_listing) {
    private lateinit var binding: FragmentItemListingBinding
    private lateinit var binding2: ParentItemBinding
    private lateinit var itemListingAdapter:ItemListingAdapter
    private val args by navArgs<ItemListingFragmentArgs>()
    private lateinit var restaurant:RestaurantModel
    private val viewModel by viewModels<MenuViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = FragmentItemListingBinding.inflate(inflater)
        binding2 = ParentItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurant = args.restaurant
        viewModel.fetchItem(restaurant.id)

        setUpRv()

        itemListingAdapter.onClick = { item ->
            val b=Bundle().apply { putParcelable("item",item)
                putParcelable("owner",restaurant)
            putString("restaurant",restaurant.id)}
            val actionId = R.id.action_itemListingFragment_to_foodListingFragment
            Log.d("Navigation", "Action ID: $actionId")
            findNavController().navigate(actionId, b)
        }

        lifecycleScope.launch {
            viewModel.item.collectLatest {
                when(it){
                    is Resource.Success -> {
                        itemListingAdapter.differ2.submitList(it.data)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setUpRv() {
        itemListingAdapter = ItemListingAdapter()
        binding.itemrv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = itemListingAdapter
        }
    }
    override fun onResume() {
        super.onResume()
        Log.e("PRessed","Error")
        // Add back button listener
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Navigate up when back button is pressed
            findNavController().navigate(R.id.homeFragment)
        }
    }


}