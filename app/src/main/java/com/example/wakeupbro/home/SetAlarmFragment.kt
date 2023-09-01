package com.example.wakeupbro.ui.home

import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.wakeupbro.R
import com.example.wakeupbro.data.Datastore
import com.example.wakeupbro.databinding.FragmentSetAlarmBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/*
    Set the alarm with all the options
 */
class SetAlarmFragment : Fragment() {


    private lateinit var binding : FragmentSetAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetAlarmBinding.inflate( inflater )

        // set alarm listener
        binding.btnSetTime.setOnClickListener {
            showTimePickerDialog()
        }

        // set the selection if selected before
        CoroutineScope( Dispatchers.Main ).launch {


            Datastore.getBooleanValues(requireContext(),Datastore.math).collect { value ->
                if (value) {
                    binding.methods.methodMaths.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.primary)
                }
                else{
                    binding.methods.methodMaths.backgroundTintList = binding.methods.methodMaths.backgroundTintList?.let { ColorStateList.valueOf( it.defaultColor) }
                }
            }
        }

        // set maths
        binding.methods.methodMaths.setOnClickListener {
            findNavController().navigate(R.id.methodMathsSetting)
        }

        // set Qr
        binding.methods.methodQr.setOnClickListener{
//            findNavController().navigate(R.id.QRScanner)
        }

        return binding.root
    }

    // show the timer picker to set the time.
    private fun showTimePickerDialog() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        // Ticker picker that takes the context, listener, default hour, minute and hourFormat
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->

                val formattedTime = formatTime(selectedHour, selectedMinute)
                val tv_alarm_time = view?.findViewById<TextView>(R.id.tv_alarm_time)
                tv_alarm_time?.text = formattedTime },
            hour,
            minute,
            false // Set to true if you want to use 24-hour format, false for 12-hour format
        )

        // show the dialog
        timePickerDialog.show()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return timeFormat.format(cal.time)
    }
}