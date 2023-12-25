package com.example.foodgasm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodgasm.data.RestaurantModel
import com.example.foodgasm.databinding.ChildItemBinding
import com.squareup.picasso.Picasso

class FoodListingAdapter() : RecyclerView.Adapter<FoodListingAdapter.FoodListingViewHolder>() {

    inner class FoodListingViewHolder( val binding: ChildItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: RestaurantModel){
            binding.apply {
                Log.d("ImageURL", "Image URL: ${item.image}")
                Picasso.get().load(item.image).into(image)
                name.text=item.name.toString()
            }
        }
    }

    val diffUtil2 = object: DiffUtil.ItemCallback<RestaurantModel>() {
        override fun areItemsTheSame(oldItem: RestaurantModel, newItem: RestaurantModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RestaurantModel,
            newItem: RestaurantModel
        ): Boolean {
            return oldItem==newItem
        }

    }

    val differ2 = AsyncListDiffer(this,diffUtil2)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListingViewHolder {
        val binding= ChildItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FoodListingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ2.currentList.size
    }

    override fun onBindViewHolder(holder: FoodListingViewHolder, position: Int) {
        val item = differ2.currentList[position]
        item.let {
            holder.bind(it)
        }
        holder.binding.root.setOnClickListener {
            onClick?.invoke(item)

        }

    }
    var onClick: ((RestaurantModel)-> Unit)?=null

}