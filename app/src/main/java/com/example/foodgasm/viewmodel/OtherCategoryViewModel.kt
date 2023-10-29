package com.example.foodgasm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodgasm.data.RestaurantModel
import com.example.foodgasm.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.View
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OtherCategoryViewModel @Inject constructor(
   private val firestore: FirebaseFirestore
):ViewModel() {
    private val _chineseRes = MutableStateFlow<Resource<List<RestaurantModel>>>(Resource.Initial())
    val chineseRes: StateFlow<Resource<List<RestaurantModel>>> = _chineseRes

    private val _nepaliRes = MutableStateFlow<Resource<List<RestaurantModel>>>(Resource.Initial())
    val nepaliRes: StateFlow<Resource<List<RestaurantModel>>> = _nepaliRes

    private val _healthRes = MutableStateFlow<Resource<List<RestaurantModel>>>(Resource.Initial())
    val healthRes: StateFlow<Resource<List<RestaurantModel>>> = _healthRes


    init {
        fetchChineseRes()
        fetchNepaliRes()
        fetchHealthFoods()
    }

    private fun fetchHealthFoods() {
        viewModelScope.launch {
            _healthRes.emit(Resource.Loading())
        }


        firestore.collection("Restaurant").whereEqualTo("category","Healthy")
            .get().addOnSuccessListener { result->
                val featuredList = result.toObjects(RestaurantModel::class.java)
                viewModelScope.launch{
                    _healthRes.emit(Resource.Success(featuredList))
                }
            }.addOnFailureListener {
                viewModelScope.launch{
                    _healthRes.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchNepaliRes() {
        viewModelScope.launch {
            _nepaliRes.emit(Resource.Loading())
        }


        firestore.collection("Restaurant").whereEqualTo("category","Nepali")
            .get().addOnSuccessListener { result->
                val featuredList = result.toObjects(RestaurantModel::class.java)
                viewModelScope.launch{
                    _nepaliRes.emit(Resource.Success(featuredList))
                }
            }.addOnFailureListener {
                viewModelScope.launch{
                    _nepaliRes.emit(Resource.Error(it.message.toString()))
                }
            }
    }


    fun fetchChineseRes(){
        viewModelScope.launch {
            _chineseRes.emit(Resource.Loading())
        }


        firestore.collection("Restaurant").whereEqualTo("category","Chinese")
            .get().addOnSuccessListener { result->
                val featuredList = result.toObjects(RestaurantModel::class.java)
                viewModelScope.launch{
                    _chineseRes.emit(Resource.Success(featuredList))
                }
            }.addOnFailureListener {
                viewModelScope.launch{
                    _chineseRes.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}