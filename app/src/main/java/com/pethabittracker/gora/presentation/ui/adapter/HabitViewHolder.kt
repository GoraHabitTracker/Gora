package com.pethabittracker.gora.presentation.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
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
    private val onDoneClicked: (Habit) -> Unit,
    private val onSkipClicked: (Habit) -> Unit,
    private val onQuestionClicked: (Habit) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: Habit) {
        with(binding) {
            imageView.setImageResource(habit.urlImage)

            // у нас два разных стиля для замены цвета (тут один, а в CalendarFragment другой). нужно выбрать один и его придерживаться
            val colorInt: Int = context.getColor(R.color.transparent_90)
            val csl = ColorStateList.valueOf(colorInt)

            // на новый день надо будет через workmanager обновлять приоритет в привычках
            when (habit.priority) {
                Priority.Default.value -> {
                    frameChoice.isVisible = true
                    frameDone.isVisible = false
                    frameSkip.isVisible = false
                    frameInactive.isVisible = false
                    root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    root.setCardForegroundColor(ColorStateList.valueOf(context.getColor(R.color.transparent)))
                    tvNameHabit.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,ContextCompat.getDrawable(context, R.drawable.icon_question_periwinkle),null
                    )
                }
                Priority.Done.value -> {
                    frameChoice.isVisible = false
                    frameDone.isVisible = true
                    frameSkip.isVisible = false
                    frameInactive.isVisible = false
                    imageView.foreground = null
                    root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pastel_green))
                    root.setCardForegroundColor(ColorStateList.valueOf(context.getColor(R.color.transparent)))
                    tvNameHabit.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,ContextCompat.getDrawable(context, R.drawable.icon_question_white),null
                    )
                }
                Priority.Skip.value -> {
                    frameChoice.isVisible = false
                    frameDone.isVisible = false
                    frameSkip.isVisible = true
                    frameInactive.isVisible = false
                    root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    root.setCardForegroundColor(csl)
                    tvNameHabit.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,ContextCompat.getDrawable(context, R.drawable.icon_question_blue_fcbk),null
                    )
                }
                Priority.Inactive.value -> {
                    frameChoice.isVisible = false
                    frameDone.isVisible = false
                    frameSkip.isVisible = false
                    frameInactive.isVisible = true
                    root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    root.setCardForegroundColor(ColorStateList.valueOf(context.getColor(R.color.transparent)))
                    tvNameHabit.setCompoundDrawablesWithIntrinsicBounds(
                        null,null,ContextCompat.getDrawable(context, R.drawable.icon_question_periwinkle),null
                    )
                }
            }

            buttonDone.setOnClickListener {
                buttonDone.isVisible = false
                buttonSkip.isVisible = false
                frameDone.isVisible = true
                root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.pastel_green))

                onDoneClicked(habit)
            }

            buttonSkip.setOnClickListener {
                buttonDone.isVisible = false
                buttonSkip.isVisible = false
                frameSkip.isVisible = true
                root.setCardForegroundColor(csl)
                
                onSkipClicked(habit)
            }

            tvNameHabit.setOnClickListener {
                onQuestionClicked(habit)
            }

            tvNameHabit.text = habit.name
        }
    }
}
