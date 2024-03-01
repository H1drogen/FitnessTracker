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

        // Set any additional properties or event listeners for the CalendarView if needed
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Handle the selected date change
            // This is just an example, you can implement your logic here
            Toast.makeText(
                requireContext(),
                "Selected date: $year-$month-$dayOfMonth",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
