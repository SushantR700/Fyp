package com.example.foodgasm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodgasm.data.CartProduct
import com.example.foodgasm.firebase.FirebaseCommon
import com.example.foodgasm.utils.Resource
import com.google.android.play.core.integrity.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Initial())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct){
        viewModelScope.launch { _addToCart.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart").whereEqualTo("restaurant.id",cartProduct.restaurant.id)
            .get().addOnSuccessListener {
                it.documents.let{
                    if(it.isEmpty()){ //Add one product
                        addNewProduct(cartProduct)
                    }else{
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct){ //Increase the quantity
                            val documentId = it.first().id
                            increaseQuantity(documentId, cartProduct)
                        }else { //Add new product
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToCart.emit(Resource.Error(it.message.toString())) }

            }
    }
private fun addNewProduct(cartProduct: CartProduct){
    firebaseCommon.addProductToCart(cartProduct){
        addedProduct, e->
        viewModelScope.launch {
            if(e==null){
                _addToCart.emit(Resource.Success(addedProduct!!))
            }else{
                _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}
    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(documentId) { _, e ->
            viewModelScope.launch {
                if (e == null) {
                    _addToCart.emit(Resource.Success(cartProduct!!))
                } else {
                    _addToCart.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }

}