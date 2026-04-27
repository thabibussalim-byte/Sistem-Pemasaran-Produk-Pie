package com.example.zliepie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.zliepie.R
import com.example.zliepie.databinding.FragmentPaymentBinding
import java.text.NumberFormat
import java.util.Locale

@Suppress("DEPRECATION")
class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ZLiePieViewModel by viewModels {
        ZLiePieViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            if (items.isNullOrEmpty()) {
                binding.orderSummaryText.text = "Tidak ada item untuk dibayar."
                binding.totalPriceText.text = "Total: Rp 0"
                binding.payButton.isEnabled = false
            } else {
                val summary = StringBuilder()
                var total = 0.0
                items.forEach { item ->
                    val itemTotal = item.price * item.quantity
                    summary.append("${item.productName} (x${item.quantity}) - ${numberFormat.format(itemTotal)}\n")
                    total += itemTotal
                }
                binding.orderSummaryText.text = summary.toString()
                binding.totalPriceText.text = "Total Tagihan: ${numberFormat.format(total)}"
                binding.payButton.isEnabled = true
            }
        }

        binding.payButton.setOnClickListener {
            val selectedId = binding.paymentMethodGroup.checkedRadioButtonId
            val radioButton = view.findViewById<RadioButton>(selectedId)
            val paymentMethod = radioButton?.text ?: "Tidak dipilih"

            viewModel.checkout()
            
            Toast.makeText(requireContext(), 
                "Pembayaran Berhasil via $paymentMethod! Pie Anda sedang diproses.", 
                Toast.LENGTH_LONG).show()

            if (findNavController().currentDestination?.id == R.id.paymentFragment) {
                findNavController().navigate(R.id.action_paymentFragment_to_catalogFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
