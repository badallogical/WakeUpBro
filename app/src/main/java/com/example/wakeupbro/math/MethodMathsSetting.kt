package com.example.wakeupbro.ui.math

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wakeupbro.data.Datastore
import com.example.wakeupbro.databinding.FragmentMethodMathsSettingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MethodMathsSetting : Fragment() {

    private lateinit var binding : FragmentMethodMathsSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentMethodMathsSettingBinding.inflate(inflater)

        // save the math method to datastore
        binding.btnProceed.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                Datastore.saveMethodMaths(requireContext(), true)
                findNavController().navigateUp()
            }
        }

        // remove the method
        binding.btnRemove.setOnClickListener {
            CoroutineScope( Dispatchers.Main).launch {
                Datastore.saveMethodMaths(requireContext(), false)
                findNavController().navigateUp()
            }
        }

        return binding.root
    }
}