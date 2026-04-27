package com.example.zliepie.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zliepie.NotificationHelper
import com.example.zliepie.R
import com.example.zliepie.data.repository.Product
import com.example.zliepie.databinding.FragmentCatalogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CatalogFragment : Fragment() {
    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZLiePieViewModel by viewModels {
        ZLiePieViewModel.Factory(requireActivity().application)
    }

    private lateinit var notificationHelper: NotificationHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Izin notifikasi diberikan.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "Izin notifikasi ditolak.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationHelper = NotificationHelper(requireContext())
        checkNotificationPermission()

        val adapter = ProductAdapter { product ->
            showAddToCartDialog(product)
        }

        binding.productRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
        }

        viewModel.allProducts.observe(viewLifecycleOwner) { products ->
            adapter.submitList(products)
        }

        binding.cartButton.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.catalogFragment) {
                findNavController().navigate(R.id.action_catalogFragment_to_cartFragment)
            }
        }
    }

    private fun showAddToCartDialog(product: Product) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Tambah ke Keranjang?")
            .setMessage("Apakah Anda ingin menambahkan ${product.name} seharga Rp ${product.price} ke keranjang?")
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Tambah") { _, _ ->
                viewModel.addToCart(product)
                notificationHelper.showCartNotification(product.name, product.imageLocal)
                Toast.makeText(requireContext(), "${product.name} berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
