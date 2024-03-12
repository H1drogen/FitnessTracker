package com.fdmgroup.FitnessTrackerApp

import ActivityLog
import ActivityLogDbHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment

class DataVisualisationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data_visualisation, container, false)

        // Initialize Spinner and TableLayout
        val spinner: Spinner = view.findViewById(R.id.spinner_dropdown)
        val tableLayout: TableLayout = view.findViewById(R.id.table_layout)

        // Query distinct activities from the database
        val dbHelper = ActivityLogDbHelper(requireContext())
        val dropdownItems = dbHelper.getDistinctActivities().toTypedArray()

        // Create ArrayAdapter
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

                // Update the table with the retrieved data
                updateTable(activityLogs, tableLayout)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }

        return view
    }

    private fun updateTable(activityLogs: List<ActivityLog>, tableLayout: TableLayout) {
        // Clear existing rows in the table
        tableLayout.removeAllViews()

        // Create header row
        val headerRow = TableRow(requireContext())
        val headerCells = arrayOf("Date", "Weight", "Sets", "Reps")

        for (header in headerCells) {
            val cell = createTableCell(header, true)
            headerRow.addView(cell)
        }

        tableLayout.addView(headerRow)

        // Populate the table with activity logs
        for (log in activityLogs) {
            val dataRow = TableRow(requireContext())
            val dataCells = arrayOf(log.date, log.weight.toString(), log.sets.toString(), log.reps.toString())

            for (data in dataCells) {
                val cell = createTableCell(data, false)
                dataRow.addView(cell)
            }

            tableLayout.addView(dataRow)
        }
    }

    private fun createTableCell(text: String, isHeader: Boolean): TextView {
        val cell = TextView(requireContext())
        cell.text = text
        cell.setPadding(16, 8, 16, 8)
        cell.setBackgroundResource(if (isHeader) R.drawable.table_header_bg else R.drawable.table_cell_bg)
        return cell
    }
}