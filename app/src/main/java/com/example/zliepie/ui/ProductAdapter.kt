package com.example.zliepie.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zliepie.data.repository.Product
import com.example.zliepie.databinding.ItemProductBinding

class ProductAdapter(private val onAddClick: (Product) -> Unit) :
    ListAdapter<Product, ProductAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product, onAddClick: (Product) -> Unit) {
            binding.apply {
                productName.text = product.name
                productPrice.text = "Rp ${product.price}"
                productImage.setImageResource(product.imageLocal)
                btnAddToCart.setOnClickListener { onAddClick(product) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onAddClick)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    }
}
