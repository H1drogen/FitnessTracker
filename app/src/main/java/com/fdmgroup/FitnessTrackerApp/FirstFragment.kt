package com.fdmgroup.FitnessTrackerApp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fdmgroup.FitnessTrackerApp.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var isEditMode = false
    private var editingTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.addButton.setOnClickListener {
            addBox() // Handles both adding and editing
        }
        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.Activity_Logger_to_Calendar)
        }
        binding.dataVisualisationButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_DataVisualisationFragment)
        }
        setupLongClickListeners()

        return binding.root
    }


    private fun addBox() {
        val inputText = binding.inputEditText.text.toString()
        binding.inputEditText.text.clear() // Clear input text immediately after retrieval

        if (isEditMode && editingTextView != null) {
            // Edit mode - Update the existing TextView
            if (inputText.isNotEmpty()) {
                editingTextView?.text = inputText
                Toast.makeText(requireContext(), "Goal updated", Toast.LENGTH_SHORT).show()
            }
            // Important: Reset edit mode and editingTextView here
            isEditMode = false
            editingTextView = null
        } else if (inputText.isNotEmpty()) {
            // Add mode - Add a new TextView
            val newBox = TextView(requireContext()).apply {
                text = inputText
                setOnLongClickListener {
                    // Assuming showOptionsDialog sets isEditMode and editingTextView
                    showOptionsDialog(this)
                    true
                }
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                setBackgroundResource(R.drawable.small_box_background)
                setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
                layoutParams = LinearLayout.LayoutParams(dpToPx(100), dpToPx(100)).apply {
                    setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4))

                }
            }
            (binding.boxContainer.getChildAt(0) as LinearLayout).addView(newBox)
            Toast.makeText(requireContext(), "Goal added", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please enter text", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showOptionsDialog(textView: TextView) {
        val options = arrayOf("Edit", "Delete")
        AlertDialog.Builder(requireContext())
            .setTitle("Choose an option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> editBox(textView) // Edit option
                    1 -> removeBox(textView) // Delete option
                }
            }
            .show()
    }

    private fun editBox(textView: TextView) {
        isEditMode = true
        editingTextView = textView

        // Populate the EditText with the text from the TextView
        binding.inputEditText.setText(textView.text)
        // No need to set a temporary listener here
    }


    private fun removeBox(view: View) {
        (binding.boxContainer.getChildAt(0) as LinearLayout).removeView(view)
        Toast.makeText(requireContext(), "Goal removed", Toast.LENGTH_SHORT).show()
    }
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
        ).toInt()

    }
    private fun setupLongClickListeners() {
        val linearLayout = binding.boxContainer.getChildAt(0) as LinearLayout
        for (i in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(i)
            if (child is TextView) { // Check if the child is a TextView
                child.setOnLongClickListener {
                    showOptionsDialog(child)
                    true
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
