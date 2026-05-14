package com.example.paryavarankavalu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.paryavarankavalu.databinding.FragmentProfileBinding
import com.example.paryavarankavalu.viewmodels.ReportViewModel
import com.example.paryavarankavalu.viewmodels.ReportViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportViewModel by viewModels {
        ReportViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUser()
    }

    private fun observeUser() {
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                binding.pointsText.text = "Total Eco-Karma Points: ${user.totalPoints}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}