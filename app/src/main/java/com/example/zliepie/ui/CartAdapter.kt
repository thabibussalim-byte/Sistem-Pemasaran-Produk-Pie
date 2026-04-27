package com.example.zliepie.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zliepie.data.dataclass.CartItem
import com.example.zliepie.databinding.ItemCartBinding

class CartAdapter(
    private val onRemoveClick: (CartItem) -> Unit,
    private val onIncreaseClick: (CartItem) -> Unit,
    private val onDecreaseClick: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            item: CartItem,
            onRemoveClick: (CartItem) -> Unit,
            onIncreaseClick: (CartItem) -> Unit,
            onDecreaseClick: (CartItem) -> Unit
        ) {
            binding.apply {
                cartProductName.text = item.productName
                cartProductPrice.text = "Rp ${item.price}"
                quantityText.text = item.quantity.toString()
                
                removeButton.setOnClickListener { onRemoveClick(item) }
                increaseButton.setOnClickListener { onIncreaseClick(item) }
                decreaseButton.setOnClickListener { onDecreaseClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onRemoveClick, onIncreaseClick, onDecreaseClick)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem == newItem
    }
}
