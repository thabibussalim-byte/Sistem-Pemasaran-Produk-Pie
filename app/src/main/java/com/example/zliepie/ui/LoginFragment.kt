package com.example.zliepie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.zliepie.R
import com.example.zliepie.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZLiePieViewModel by viewModels {
        ZLiePieViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            
            if (username.isEmpty()) {
                binding.usernameLayout.error = "Username tidak boleh kosong"
                return@setOnClickListener
            } else {
                binding.usernameLayout.error = null
            }

            viewModel.login(username, password)
        }

        binding.goToRegisterText.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.loginFragment) {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

        viewModel.loginStatus.observe(viewLifecycleOwner) { result ->
            result?.let {
                if (it.isSuccess) {
                    if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_catalogFragment)
                        viewModel.resetLoginStatus()
                    }
                } else {
                    Toast.makeText(requireContext(), it.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
                    viewModel.resetLoginStatus()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
