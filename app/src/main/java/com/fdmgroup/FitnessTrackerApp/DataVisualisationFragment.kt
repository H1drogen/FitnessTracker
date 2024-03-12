package com.fdmgroup.FitnessTrackerApp

import ActivityLogDbHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment

class DataVisualisationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data_visualisation, container, false)

        // Initialize Spinner and ArrayAdapter
        val spinner: Spinner = view.findViewById(R.id.spinner_dropdown)

        val dbHelper = ActivityLogDbHelper(requireContext())
        dbHelper.clearAllData()
        dbHelper.addDummyData()
        val dropdownItems = dbHelper.getDistinctActivities().toTypedArray()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dropdownItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Handle Spinner item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Handle the selected item here
                val selectedActivity = dropdownItems[position]

                // Retrieve data for the selected activity from the database
                val activityLogs = dbHelper.getActivityLogsForActivity(selectedActivity)

                // Now you can use the 'activityLogs' list as needed, for example, display it in a RecyclerView
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }

        return view
    }
}