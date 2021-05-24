package com.unipi.p17172.emarket.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unipi.p17172.emarket.databinding.FragmentMyCartBinding

class MyCartFragment : Fragment() {
    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var _binding: FragmentMyCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun init() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}