package com.pethabittracker.gora.presentation.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCalendarBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setBackgroundResource(R.color.sea_foam)
        binding.root.setBackgroundColor(Color.BLUE)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
