package com.example.zliepie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.zliepie.databinding.FragmentRegisterBinding
import androidx.navigation.fragment.findNavController

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZLiePieViewModel by viewModels {
        ZLiePieViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            val username = binding.registerUsernameInput.text.toString()
            val password = binding.registerPasswordInput.text.toString()
            
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            viewModel.register(username, password)
        }

        binding.backToLoginText.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.registerStatus.observe(viewLifecycleOwner) { result ->
            result?.let {
                if (it.isSuccess) {
                    Toast.makeText(requireContext(), "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    viewModel.resetRegisterStatus()
                } else {
                    Toast.makeText(requireContext(), it.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetRegisterStatus()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
