package com.example.zliepie.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zliepie.R
import com.example.zliepie.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZLiePieViewModel by viewModels {
        ZLiePieViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CartAdapter(
            onRemoveClick = { item ->
                viewModel.removeFromCart(item)
                Toast.makeText(requireContext(), "${item.productName} dihapus", Toast.LENGTH_SHORT).show()
            },
            onIncreaseClick = { item ->
                viewModel.updateCartItem(item.copy(quantity = item.quantity + 1))
            },
            onDecreaseClick = { item ->
                if (item.quantity > 1) {
                    viewModel.updateCartItem(item.copy(quantity = item.quantity - 1))
                } else {
                    viewModel.removeFromCart(item)
                    Toast.makeText(requireContext(), "${item.productName} dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            val total = items.sumOf { it.price * it.quantity }
            binding.totalPriceText.text = "Rp $total"
        }

        binding.checkoutButton.setOnClickListener {
            if (viewModel.cartItems.value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Keranjang kosong", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_cartFragment_to_paymentFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
