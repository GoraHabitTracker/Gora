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
    private val onButtonActionClicked: (Habit, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: Habit) {

        with(binding) {

            imageView.setImageResource(habit.urlImage)

            frameChoice.isVisible = true

            buttonDone.setOnClickListener {
                frameChoice.isVisible = false
                frameDone.isVisible = true
                root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pastel_green))

                onButtonActionClicked(habit, 2)     // может добавить отдельный класс\сущность Priority? для лучше читаемости кода
            }

            buttonSkip.setOnClickListener {
                frameChoice.isVisible = false
                frameSkip.isVisible = true
                root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_2))

                onButtonActionClicked(habit, 1)
            }

            tvNameHabit.text = habit.name
        }
    }
}
