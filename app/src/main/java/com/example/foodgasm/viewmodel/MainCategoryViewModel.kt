package com.example.foodgasm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodgasm.data.RestaurantModel
import com.example.foodgasm.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) :ViewModel() {
    private val _featuredRes = MutableStateFlow<Resource<List<RestaurantModel>>>(Resource.Initial())
    val featuredRes:StateFlow<Resource<List<RestaurantModel>>> = _featuredRes

    private val _allRes = MutableStateFlow<Resource<List<RestaurantModel>>>(Resource.Initial())
    val allRes:StateFlow<Resource<List<RestaurantModel>>> = _allRes

    init {
        fetchFeaturedProducts()
        fetchallRes()
    }

    private fun fetchallRes() {
        viewModelScope.launch {
            _allRes.emit(Resource.Loading())
        }

        firestore.collection("Restaurant").whereNotEqualTo("category","Featured")
            .get().addOnSuccessListener {
                val resList= it.toObjects(RestaurantModel::class.java)
                viewModelScope.launch{
                    _allRes.emit(Resource.Success(resList))
                }
            }.addOnFailureListener {
                viewModelScope.launch{
                    _allRes.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchFeaturedProducts(){
        viewModelScope.launch {
            _featuredRes.emit(Resource.Loading())
        }


        firestore.collection("Restaurant").whereEqualTo("category","Featured")
            .get().addOnSuccessListener { result->
                val featuredList = result.toObjects(RestaurantModel::class.java)
                viewModelScope.launch{
                    _featuredRes.emit(Resource.Success(featuredList))
                }
            }.addOnFailureListener {
                viewModelScope.launch{
                    _featuredRes.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}