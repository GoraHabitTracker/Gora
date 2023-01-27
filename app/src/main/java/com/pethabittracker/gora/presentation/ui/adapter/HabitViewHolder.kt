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
    private val onButtonDoneClicked: (Habit) -> Unit,
    private val onButtonSkipClicked: (Habit) -> Unit
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

                onButtonDoneClicked(habit)
            }

            buttonSkip.setOnClickListener {
                frameChoice.isVisible = false
                frameSkip.isVisible = true
                root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_2))

                onButtonSkipClicked(habit)
            }

            tvNameHabit.text = habit.name
        }
    }
}
