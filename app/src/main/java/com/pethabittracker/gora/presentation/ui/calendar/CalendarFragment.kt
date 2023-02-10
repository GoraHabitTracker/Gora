package com.pethabittracker.gora.presentation.ui.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.pethabittracker.gora.R
import com.pethabittracker.gora.data.utils.displayText
import com.pethabittracker.gora.data.utils.getCurrentDate
import com.pethabittracker.gora.data.utils.setTextColorRes
import com.pethabittracker.gora.databinding.CalendarDayLayoutBinding
import com.pethabittracker.gora.databinding.FragmentCalendarBinding
import com.pethabittracker.gora.presentation.models.MonthViewContainer
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel by viewModel<CalendarViewModel>()

    private val monthCalendarView: CalendarView get() = binding.calendarView
    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()

    // список дат с выполненными привычками
    private var dateFulfilldHabits = emptyList<LocalDate>()

    // список дат с НЕвыполненными привычками
    private var dateUnfulfilledHabits = emptyList<LocalDate>()

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

        val daysOfWeek = daysOfWeek()
        binding.calendarView.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].displayText()
                textView.setTextColorRes(R.color.sapphire)
            }

        dateFulfilldHabits =
            viewModel.getDateWithFulfilledHabitFlow()  // эти строки отрабатывают после отресовки view календаря
        dateUnfulfilledHabits = viewModel.getDateWithUnfulfilledHabitFlow()

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)
    }

    private fun setupMonthCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            /** Will be set when this container is bound. See the dayBinder. */
            lateinit var day: CalendarDay
            val textView = CalendarDayLayoutBinding.bind(view).calendarDayText

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        dateClicked(date = day.date)
                    }
                }
            }
        }

        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    /**
                     * Remember that the header is reused so this will be called for each month.
                     * However, the first day of the week will not change so no need to bind
                     * the same view every time it is reused.
                     * */

                    if (container.titlesContainer.tag == null) {
                        container.titlesContainer.tag = data.yearMonth
                        container.titlesContainer.children.map { it as TextView }
                            .forEachIndexed { index, textView ->
                                textView.text = daysOfWeek[index].displayText()
                                textView.setTextColorRes(R.color.cross_color)
                                /**
                                 * In the code above, we use the same `daysOfWeek` list
                                 * that was created when we set up the calendar.
                                 * val daysOfWeek = data.weekDays.first().map { it.date.dayOfWeek }
                                 * Alternatively, you can get the value for this specific index:
                                 * val dayOfWeek = data.weekDays.first()[index].date.dayOfWeek
                                 * */
                            }
                    }
                }

                override fun create(view: View): MonthViewContainer {
                    return MonthViewContainer(view)
                }
            }

        monthCalendarView.apply {
            dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.day = data
                    bindDate(data.date, container.textView, data.position == DayPosition.MonthDate)
                }
            }
            monthScrollListener = { updateTitle() }
            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)
        }
    }

    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        textView.text = date.dayOfMonth.toString()
        if (isSelectable) {
            when {
                selectedDates.contains(date) -> {
                    textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                }
                today == date -> {
                    textView.setTextColorRes(R.color.sapphire)
                    textView.setBackgroundResource(R.drawable.background_today_ring)
                }
                else -> {
                    textView.setTextColorRes(R.color.sapphire)
                    textView.background = null
                }
            }
        } else {
            textView.setTextColorRes(R.color.cross_color)
            textView.background = null
        }

        when {  // TODO Календарь отрабатывает только со второго захода. Надо исправить
            // 1 сегодня
            date == today -> {
                textView.setBackgroundResource(R.drawable.background_today_ring)
            }
            // 2 - привычки все сделаны
            dateFulfilldHabits.contains(date) -> {
                textView.setBackgroundResource(R.drawable.background_calendar_done)
            }
            // 3 - одна из привычек не сделана
            dateUnfulfilledHabits.contains(date) -> {
                textView.setTextColorRes(R.color.snow_white)
                textView.setBackgroundResource(R.drawable.background_calendar_skipped)
            }
            // 0 - ничего не меняем. Привычки не были созданы в этот день
            else -> {}
        }

        val myDate = LocalDate.parse("2023-02-03")
        if (myDate == date) {
            textView.setBackgroundResource(R.drawable.background_calendar_done)
        }
        val myDat2e = LocalDate.parse("2023-02-23")
        if (myDat2e == date) {
            textView.setTextColorRes(R.color.snow_white)
            textView.setBackgroundResource(R.drawable.background_calendar_skipped)
        }
    }

    private fun dateClicked(date: LocalDate) {
        if (selectedDates.contains(date)) {
            selectedDates.remove(date)
        } else {
            selectedDates.add(date)
        }
        /** Refresh both calendar views.. */
        monthCalendarView.notifyDateChanged(date)
    }

    @SuppressLint("SetTextI18n")
    private fun updateTitle() {
        binding.todaysDateText.text = getCurrentDate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
