package com.pethabittracker.gora.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pethabittracker.gora.databinding.ItemHabitBinding
import com.pethabittracker.gora.domain.models.Habit

class HabitAdapter(
    private val context: Context,
    private val onDoneClicked: (Habit) -> Unit,
    private val onSkipClicked: (Habit) -> Unit
) : ListAdapter<Habit, HabitViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(
            ItemHabitBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            ),
            context = context,
            onSkipClicked = onSkipClicked,
            onDoneClicked = onDoneClicked
        )
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.urlImage == newItem.urlImage
            }
        }
    }
}
