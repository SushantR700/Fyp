package com.example.foodgasm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.foodgasm.data.RestaurantModel
import com.example.foodgasm.databinding.CategoryRvItemBinding
import com.example.foodgasm.databinding.FeatureRvItemBinding
import com.example.foodgasm.databinding.RestaurantRvItemBinding
import com.squareup.picasso.Picasso
import java.net.URLEncoder

class ChineseAdapter:RecyclerView.Adapter<ChineseAdapter.ChineseViewHolder>() {

    inner class ChineseViewHolder(val binding:CategoryRvItemBinding ):RecyclerView.ViewHolder(binding.root){

        fun bind(restaurant:RestaurantModel){
            binding.apply {
                Log.d("Glide", "Loading image: ${restaurant.image}")
                Picasso.get().load(restaurant.image).into(img)
                tvAdName.text=restaurant.name.toString()
                address.text=restaurant.address.toString()

            }
        }
    }

    val diffUtil = object : ItemCallback<RestaurantModel>(){
        override fun areItemsTheSame(oldItem: RestaurantModel, newItem: RestaurantModel): Boolean {
          return ( oldItem.id ==newItem.id)
        }

        override fun areContentsTheSame(
            oldItem: RestaurantModel,
            newItem: RestaurantModel
        ): Boolean {
            return (oldItem==newItem)
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChineseViewHolder {
        return ChineseViewHolder(
            CategoryRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ChineseViewHolder, position: Int) {
        val restaurant = differ.currentList[position]
        holder.bind(restaurant)
    }
}