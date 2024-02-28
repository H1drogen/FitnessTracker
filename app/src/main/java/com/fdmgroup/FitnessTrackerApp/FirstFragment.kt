package com.fdmgroup.FitnessTrackerApp

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fdmgroup.FitnessTrackerApp.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.addButton.setOnClickListener {
            addBox()
        }

        return binding.root
    }

    private fun addBox() {
        val newBox = TextView(requireContext()).apply {
            text = "New Box"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            setBackgroundResource(R.drawable.small_box_background)
            setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
            layoutParams = LinearLayout.LayoutParams(dpToPx(100), dpToPx(100)).apply {
                setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4))
            }
        }

        (binding.boxContainer.getChildAt(0) as LinearLayout).addView(newBox)
        Toast.makeText(requireContext(), "New box added", Toast.LENGTH_SHORT).show()
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
        ).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
