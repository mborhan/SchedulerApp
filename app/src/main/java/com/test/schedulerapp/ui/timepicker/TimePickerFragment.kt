package com.test.schedulerapp.ui.timepicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.schedulerapp.R
import com.test.schedulerapp.databinding.FragmentTimePickerBinding

class TimePickerFragment : Fragment() {

    private lateinit var binding: FragmentTimePickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimePickerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            val time = "$hourOfDay" + " : " + "$minute"
            val msg = getString(R.string.time_is) + " " + time
            binding.timeText.text = msg
        }

        binding.setupButton.setOnClickListener {
            handleSetupButtonClicked()
        }
    }

    private fun handleSetupButtonClicked(){
        //TODO::
    }
}