package com.fdmgroup.FitnessTrackerApp

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import java.security.AccessController.getContext


class TextBoxFragment : Fragment() {

    private lateinit var boxContainer: LinearLayout
    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_text_box, container, false)
        boxContainer = view.findViewById(R.id.boxContainer)
        addButton = view.findViewById(R.id.addButton)

        addButton.setOnClickListener {
            addBox()
        }

        return view
    }

    private fun addBox() {
        val newBox = TextView(requireContext())
        newBox.text = "New Box"
        newBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        newBox.setBackgroundResource(R.drawable.small_box_background)
        newBox.setPadding(8, 8, 8, 8)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(4, 4, 4, 4)
        newBox.layoutParams = params

        boxContainer.addView(newBox)
    }
}