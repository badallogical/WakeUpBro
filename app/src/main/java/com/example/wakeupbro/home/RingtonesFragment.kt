package com.example.wakeupbro.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.wakeupbro.databinding.FragmentRingtonesBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RingtonesFragment : Fragment() {

    private var _binding : FragmentRingtonesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        _binding = FragmentRingtonesBinding.inflate(inflater,container,false)

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        // Adapter for viewPager
        val adapter = RingtonesPagerAdapter( parentFragmentManager, lifecycle )
        viewPager.adapter = adapter

        // Connect the TabLayout with the ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Customize the tab titles as needed
            tab.text = when (position){
                0 -> "Default"
                else -> "Local"
            }
        }.attach()

        return binding.root
    }


    private inner class RingtonesPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return 2 // Number of Fragments you want to display
        }

        override fun createFragment(position: Int): Fragment {
            // Return the Fragment for each position
            return when (position) {
                0 -> DefaultRingtoneFragment()
                else -> LocalRingtonesFragment()
            }
        }
    }

}