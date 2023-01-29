package com.pethabittracker.gora.presentation.ui.adapter

import android.content.Context
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
                    root.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.transparent_2
                        )
                    )
                }
            }

            buttonDone.setOnClickListener {
                frameChoice.isVisible = false
                frameDone.isVisible = true
                root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pastel_green))

                onButtonActionClicked(
                    habit,
                    Priority.Done.value
                )
            }

            buttonSkip.setOnClickListener {
                frameChoice.isVisible = false
                frameSkip.isVisible = true
                root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_2))

                onButtonActionClicked(habit, Priority.Skip.value)
            }

            tvNameHabit.text = habit.name
        }
    }
}
