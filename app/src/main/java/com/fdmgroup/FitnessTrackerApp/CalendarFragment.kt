package com.fdmgroup.FitnessTrackerApp

import ActivityLogDbHelper
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fdmgroup.FitnessTrackerApp.databinding.FragmentCalendarBinding
import android.widget.PopupWindow
import android.widget.Button
import android.widget.EditText


class CalendarFragment: Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private var selectedDate: String? = null
    private lateinit var ActivityLogDbHelper: ActivityLogDbHelper

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
        val addButton = binding.floatingActionButton2
        ActivityLogDbHelper = ActivityLogDbHelper(requireContext())

        calendarView.setOnDateChangeListener { view, year, month, day ->
            // Handle the selected date change
            selectedDate = "$day-$month-$year"
            Toast.makeText(
                requireContext(),
                "Selected date: $selectedDate",
                Toast.LENGTH_SHORT
            ).show()
        }

        addButton.setOnClickListener {
            showPopup()
        }

    }

    private fun showPopup(){
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.popup_layout, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val editTextData = popupView.findViewById<EditText>(R.id.editTextData)
        val inputSets = popupView.findViewById<EditText>(R.id.inputSets)
        val buttonSave = popupView.findViewById<Button>(R.id.buttonSave)
        val inputReps = popupView.findViewById<EditText>(R.id.inputReps)
        val inputWeights = popupView.findViewById<EditText>(R.id.inputWeight)
//        val inputDistance = popupView.findViewById<EditText>(R.id.inputDistance)

        buttonSave.setOnClickListener {
            // Retrieve data from the EditText
            val activity = editTextData.text.toString().trim()
            val date = selectedDate.toString()
            val sets = inputSets.text.toString().toIntOrNull()
            val reps = inputReps.text.toString().toIntOrNull()
            val weights = inputWeights.text.toString().toIntOrNull()
//            val distance = inputDistance.text.toString().toIntOrNull()

            if (activity.isNotEmpty()) {
                val id = ActivityLogDbHelper.insertData(date,activity,weights,sets,reps)

                if (id != -1L) {
                    showToast("Entry added to database with ID: $id")
                } else {
                    showToast("Failed to add entry to database")
                }

                popupWindow.dismiss()
            } else {
                showToast("Please enter activity")
            }
        }

        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
