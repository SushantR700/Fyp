package com.example.foodgasm.fragments.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodgasm.R
import com.example.foodgasm.adapters.FeaturedResAdapter
import com.example.foodgasm.adapters.NearByResAdapter
import com.example.foodgasm.databinding.FragmentMainCategoryBinding
import com.example.foodgasm.utils.Resource
import com.example.foodgasm.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainCategoryFragment:Fragment(R.layout.fragment_main_category) {
    private lateinit var binding:FragmentMainCategoryBinding
    private lateinit var Featuredadapter: FeaturedResAdapter
    private lateinit var Nearadapter: NearByResAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRv()
        setUpRv2()
        lifecycleScope.launch {
            viewModel.featuredRes.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        Featuredadapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                        hideLoading()
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launch{
            viewModel.allRes.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        Nearadapter.differ2.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                    }
                    else -> Unit
                }
            }
        }
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.ad1))
        imageList.add(SlideModel(R.drawable.ad2))
        imageList.add(SlideModel(R.drawable.ad3))

        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
        binding.imageSlider.setSlideAnimation(AnimationTypes.DEPTH_SLIDE)


    }

    private fun setUpRv2() {
        Nearadapter = NearByResAdapter()
        binding.restaurantRv.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter=Nearadapter
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility=View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility=View.VISIBLE
    }

    private fun setUpRv() {
        Featuredadapter=FeaturedResAdapter()
        binding.featuredRv.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=Featuredadapter
        }
    }
}