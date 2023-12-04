package com.example.foodgasm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodgasm.data.RestaurantModel
import com.example.foodgasm.fragments.ItemListingFragment
import com.example.foodgasm.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


    @HiltViewModel
    class MenuViewModel @Inject constructor(
        private val firestore: FirebaseFirestore
    ) : ViewModel() {
        private var foodId:String?=null

        private val _item = MutableStateFlow<Resource<List<RestaurantModel>>>(Resource.Initial())
        val item: StateFlow<Resource<List<RestaurantModel>>> = _item

        private val _food = MutableStateFlow<Resource<List<RestaurantModel>>>(Resource.Initial())
        val food: StateFlow<Resource<List<RestaurantModel>>> = _food


        fun fetchItem(restaurantId:String) {
            foodId=restaurantId
            viewModelScope.launch {
                _item.emit(Resource.Loading())
                firestore.collection("Restaurant").document(restaurantId).collection("Menu")
                    .get().addOnSuccessListener { result ->
                        val items = result.toObjects(RestaurantModel::class.java)
                        viewModelScope.launch {
                            _item.emit(Resource.Success(items))
                        }
                    }.addOnFailureListener {
                        viewModelScope.launch {
                            _item.emit(Resource.Error(it.message.toString()))
                        }
                    }
            }
        }

        fun fetchFood(restaurantId2:String) {
            viewModelScope.launch {
                _food.emit(Resource.Loading())
                firestore.collection("Restaurant").document(foodId!!).collection("Menu").document(restaurantId2)
                    .collection("Items")
                    .get().addOnSuccessListener { result ->
                        val items = result.toObjects(RestaurantModel::class.java)
                        viewModelScope.launch {
                            _food.emit(Resource.Success(items))
                        }
                    }.addOnFailureListener {
                        viewModelScope.launch {
                            _food.emit(Resource.Error(it.message.toString()))
                        }
                    }
            }
        }
    }

