package com.fdmgroup.FitnessTrackerApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fdmgroup.FitnessTrackerApp.databinding.FragmentCalendarBinding

class CalendarFragment: Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    val selectedDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the CalendarView using the binding
        val calendarView = binding.calendarView

        calendarView.setOnDateChangeListener { view, year, month, day ->
            // Handle the selected date change
            val selectedDate = "$day-$month-$year"
            Toast.makeText(
                requireContext(),
                "Selected date: $selectedDate",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
