package com.pethabittracker.gora.presentation.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.ItemHabitBinding
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.presentation.models.Priority

class HabitViewHolder(
    private val binding: ItemHabitBinding,
    private val context: Context,
    private val onButtonActionClicked: (Habit, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: Habit) {

        with(binding) {

            imageView.setImageResource(habit.urlImage)

            val colorInt: Int = context.getColor(R.color.transparent_3)
            val csl = ColorStateList.valueOf(colorInt)

            // на новый день надо будет через workmanager обновлять приоритет в привычках
            when (habit.priority) {
                Priority.Default.value -> {
                    frameChoice.isVisible = true
                }
                Priority.Done.value -> {
                    frameChoice.isVisible = false
                    frameDone.isVisible = true
                    root.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.pastel_green
                        )
                    )
                }
                Priority.Skip.value -> {
                    frameChoice.isVisible = false
                    frameSkip.isVisible = true
                    root.setCardForegroundColor(csl)
                }
                Priority.Inactive.value -> {
                    frameChoice.isVisible = true
                    buttonDone.isEnabled = false
                    buttonDone.isClickable = false
                    buttonSkip.isEnabled = false
                    buttonSkip.isClickable = false
                    root.setCardForegroundColor(csl)
                }
            }


            buttonDone.setOnClickListener {
                frameChoice.isVisible = false
                frameDone.isVisible = true
                root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pastel_green))

                onButtonActionClicked(habit, Priority.Done.value)
            }

            buttonSkip.setOnClickListener {
                frameChoice.isVisible = false
                frameSkip.isVisible = true
                root.setCardForegroundColor(csl)

                onButtonActionClicked(habit, Priority.Skip.value)
            }

            tvNameHabit.text = habit.name
        }
    }
}
