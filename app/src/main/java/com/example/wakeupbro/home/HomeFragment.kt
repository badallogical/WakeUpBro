package com.example.wakeupbro.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wakeupbro.R
import com.example.wakeupbro.databinding.FragmentHomeBinding

/*

 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentHomeBinding.inflate(inflater)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLetsStart.setOnClickListener{
            findNavController().navigate(R.id.setAlarmFragment)
        }

        binding.btnEdit.setOnClickListener{
            findNavController().navigate(R.id.setAlarmFragment)
        }
    }
}