package com.pethabittracker.gora.presentation.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.ItemHabitBinding
import com.pethabittracker.gora.domain.models.Habit

class HabitViewHolder(
    private val binding: ItemHabitBinding,
    private val context: Context,
    private val onButtonClicked: (Habit, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: Habit) {

        with(binding) {

            when(habit.priority){
                0 -> {
                    frameChoice.isVisible = true
                }
                1 -> {
                    frameChoice.isVisible = false
                    frameDone.isVisible = true
                    root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pastel_green))
                }
                2 -> {
                    frameChoice.isVisible = false
                    frameSkip.isVisible = true
                    root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_2))
                }
            }

            buttonDone.setOnClickListener {
                frameChoice.isVisible = false
                frameDone.isVisible = true
                root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pastel_green))

                onButtonClicked(habit,1)
            }

            buttonSkip.setOnClickListener {
                frameChoice.isVisible = false
                frameSkip.isVisible = true
                root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_2))

                onButtonClicked(habit,2)
            }

            tvNameHabit.text = habit.name
        }
    }
}
