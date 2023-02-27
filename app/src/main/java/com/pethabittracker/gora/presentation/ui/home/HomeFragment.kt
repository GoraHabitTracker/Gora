package com.pethabittracker.gora.presentation.ui.home

import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.FragmentHomeBinding
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.presentation.ui.adapter.HabitAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel by viewModel<HomeViewModel>()
    private val adapter by lazy {
        HabitAdapter(
            context = requireContext(),
            onDoneClicked = { viewModel.onDoneClicked(it) },
            onSkipClicked = { viewModel.onSkipClicked(it) },
            onQuestionClicked = { onQuestionClicked(it) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerView.adapter = adapter

            // закругляем углы картинки
            ivHills.clipToOutline = true
        }

        updateList()
        setSwipeToDelete()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateList() {

        //------------------ with Coroutine -------------------------------------------------------
        viewModel
            .getAllHabitFlow()
            .onEach { listHabits ->
                adapter.submitList(listHabits)

                if (listHabits.isNotEmpty()) {
                    binding.root.setBackgroundResource(R.color.sea_foam)
                } else {
                    binding.root.setBackgroundResource(R.color.transparent)
                }

                //для плавности замены слоёв
                delay(300)
                binding.foto.isVisible = listHabits.isEmpty()

                //просто дёргаем адаптер для пересоздания вью карточек
                binding.recyclerView.adapter = adapter

                // AlertDialog
                val thereIsIdEqualOne = listHabits.filter { it.id.id == 1 }.isNotEmpty()
                if (listHabits.size == theOnlyHabit && thereIsIdEqualOne) showAlertDialogKillHabit()
            }
            .launchIn(lifecycleScope)
    }

    private fun setSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val habit = adapter.currentList[position]
                lifecycleScope.launch {
                    viewModel.deleteHabit(habit)
                }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.5f
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val mClearPaint = Paint()
                mClearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

                val backGroundColor = Color.parseColor("#b80f0a")
                val deleteDrawable =
                    getDrawable(requireContext(), R.drawable.icon_trashcan)
                val width = deleteDrawable?.intrinsicWidth ?: 0
                val height = deleteDrawable?.intrinsicHeight ?: 0

                val itemView = viewHolder.itemView
                val itemHeight = itemView.height

                val isCancelled = (dX == 0f && !isCurrentlyActive)
                if (isCancelled) {
                    c.drawRect(
                        itemView.right + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat(),
                        mClearPaint
                    )
                }

                val valueDp = 5 * resources.displayMetrics.density
                val outR = floatArrayOf(
                    valueDp,
                    valueDp,
                    valueDp,
                    valueDp,
                    valueDp,
                    valueDp,
                    valueDp,
                    valueDp
                )
                val mBackGround = ShapeDrawable(RoundRectShape(outR, null, null))
                mBackGround.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                mBackGround.paint.color = backGroundColor
                mBackGround.draw(c)

                val deleteIconTop = itemView.top + (itemHeight - height) / 2
                val deleteIconMargin = (itemHeight - height) / 2
                val deleteIconLeft = itemView.right - deleteIconMargin - width
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + height

                deleteDrawable?.setBounds(
                    deleteIconLeft,
                    deleteIconTop,
                    deleteIconRight,
                    deleteIconBottom
                )
                deleteDrawable?.draw(c)

                super.onChildDraw(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }

    private fun showAlertDialogKillHabit() {
        val viewAlertDialogKillHabit: View =
            layoutInflater.inflate(R.layout.fragment_dialog_deleting, null, false)
        val alertDialog = AlertDialog
            .Builder(requireContext(), R.style.AlertDialogStyle)
            .setView(viewAlertDialogKillHabit)
            .show()
        viewAlertDialogKillHabit.findViewById<Button>(R.id.button_gotit).setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun onQuestionClicked(habit: Habit) {
        showAlertDialogInfoDetailed(habit)
    }

    private fun showAlertDialogInfoDetailed(habit: Habit) {
        val viewAlertDialogInfoDetailed: View =
            layoutInflater.inflate(R.layout.fragment_dialog_info_detailed, null, false)

        if (habit.repeatDays.monday) {
            viewAlertDialogInfoDetailed.findViewById<TextView>(R.id.checkOrSkip_monday_icon)
                .setCompoundDrawablesWithIntrinsicBounds(
                    getDrawable(requireContext(), R.drawable.icon_check_blue_fcbk),
                    null,
                    null,
                    null
                )
        }

        if (habit.repeatDays.thursday) {
            viewAlertDialogInfoDetailed.findViewById<TextView>(R.id.checkOrSkip_thursday_icon)
                .setCompoundDrawablesWithIntrinsicBounds(
                    getDrawable(requireContext(), R.drawable.icon_check_blue_fcbk),
                    null,
                    null,
                    null
                )
        }

        if (habit.repeatDays.wednesday) {
            viewAlertDialogInfoDetailed.findViewById<TextView>(R.id.checkOrSkip_wednesday_icon)
                .setCompoundDrawablesWithIntrinsicBounds(
                    getDrawable(requireContext(), R.drawable.icon_check_blue_fcbk),
                    null,
                    null,
                    null
                )
        }

        if (habit.repeatDays.thursday) {
            viewAlertDialogInfoDetailed.findViewById<TextView>(R.id.checkOrSkip_tuesday_icon)
                .setCompoundDrawablesWithIntrinsicBounds(
                    getDrawable(requireContext(), R.drawable.icon_check_blue_fcbk),
                    null,
                    null,
                    null
                )
        }

        if (habit.repeatDays.friday) {
            viewAlertDialogInfoDetailed.findViewById<TextView>(R.id.checkOrSkip_friday_icon)
                .setCompoundDrawablesWithIntrinsicBounds(
                    getDrawable(requireContext(), R.drawable.icon_check_blue_fcbk),
                    null,
                    null,
                    null
                )
        }

        if (habit.repeatDays.saturday) {
            viewAlertDialogInfoDetailed.findViewById<TextView>(R.id.checkOrSkip_saturday_icon)
                .setCompoundDrawablesWithIntrinsicBounds(
                    getDrawable(requireContext(), R.drawable.icon_check_blue_fcbk),
                    null,
                    null,
                    null
                )
        }

        if (habit.repeatDays.sunday) {
            viewAlertDialogInfoDetailed.findViewById<TextView>(R.id.checkOrSkip_sunday_icon)
                .setCompoundDrawablesWithIntrinsicBounds(
                    getDrawable(requireContext(), R.drawable.icon_check_blue_fcbk),
                    null,
                    null,
                    null
                )
        }
        // надо эти if'ы оптимизировать, но голова уже не соображает

        val alertDialog = AlertDialog
            .Builder(requireContext(), R.style.AlertDialogStyle)
            .setView(viewAlertDialogInfoDetailed)
            .show()

        viewAlertDialogInfoDetailed.findViewById<Button>(R.id.button_ok).setOnClickListener {
            alertDialog.dismiss()
        }
    }

    companion object {
        const val theOnlyHabit = 1
    }
}
