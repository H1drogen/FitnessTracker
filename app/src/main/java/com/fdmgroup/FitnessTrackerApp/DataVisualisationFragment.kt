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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry

import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class DataVisualisationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data_visualisation, container, false)

        // Initialize Spinner, TableLayout, and BarChart
        val spinner: Spinner = view.findViewById(R.id.spinner_dropdown)
        val tableLayout: TableLayout = view.findViewById(R.id.table_layout)
        val barChart: BarChart = view.findViewById(R.id.bar_chart)

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

                // Update the bar chart with the retrieved data
                updateBarChart(activityLogs, barChart)
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

    private fun updateBarChart(activityLogs: List<ActivityLog>, barChart: BarChart) {
        val entries = mutableListOf<BarEntry>()

        barChart.description.isEnabled = false

        // Populate entries with weight and count data
        val weightCountMap = mutableMapOf<Float, Int>()
        for (log in activityLogs) {
            val weight = log.weight.toFloat()
            weightCountMap[weight] = weightCountMap.getOrDefault(weight, 0) + 1
        }
        val sortedWeightCountMap = weightCountMap.toSortedMap()

        var index = 0f
        for ((weight, count) in sortedWeightCountMap) {
            entries.add(BarEntry(index, count.toFloat()))
            index += 1
        }

        // Create BarData for the bar chart
        val barData = BarData()
        barData.addDataSet(BarDataSet(entries, "Weight Lifted").apply {
            setDrawValues(false)
        })


        // Configure the appearance of the chart
        val xAxis = barChart.xAxis
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1.0f

        val uniqueWeights = activityLogs.map { it.weight.toFloat() }.distinct().sorted()

        xAxis.valueFormatter = IndexAxisValueFormatter(uniqueWeights.map { it.toString() }.toTypedArray())

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.setDrawGridLines(true)
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.gridLineWidth = 1.5f
        yAxisLeft.textSize = 14f

        // Set a custom value formatter for the Y-axis to display only integer values
        yAxisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value % 1 == 0f) value.toInt().toString() else ""
            }
        }

        val yAxisRight = barChart.axisRight
        yAxisRight.isEnabled = false

        barChart.legend.isEnabled = false

        // Set the BarData to the BarChart
        barChart.data = barData

        // Invalidate the chart to refresh
        barChart.invalidate()
    }
}