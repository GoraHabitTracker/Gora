package com.pethabittracker.gora.presentation.ui.home

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pethabittracker.gora.R
import com.pethabittracker.gora.data.sharedprefs.PrefsManager
import com.pethabittracker.gora.data.utils.getCurrentDate
import com.pethabittracker.gora.databinding.FragmentHomeBinding
import com.pethabittracker.gora.domain.models.Habit
import com.pethabittracker.gora.presentation.extensions.addVerticalGaps
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
            onQuestionClicked = { showAlertDialogInfoDetailed(it) }
        )
    }

    private val prefsManager by lazy {
        PrefsManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel
            .getAllHabitFlow()
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return FragmentHomeBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val linearLayoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )

            recyclerView.adapter = adapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.addVerticalGaps()
        }

        updateList()
        setSwipeToDelete()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateList() {
        viewModel
            .getAllHabitFlow()
            .onEach { listHabits ->
                adapter.submitList(listHabits)

                if (listHabits.isNotEmpty()) {
                    //для плавности замены слоёв
                    delay(300)
                    binding.recyclerView.isVisible = true
                    binding.photo.isGone = true
                    binding.ivArrow.isGone = true
                    binding.todaysDateText.text = getCurrentDate()
                    binding.todaysDateText.isGone = false
                    binding.tvClickButton.isGone = true
                } else {
                    //для плавности замены слоёв
                    delay(300)
                    binding.recyclerView.isGone = true
                    binding.photo.isVisible = true
                    binding.ivArrow.isVisible = true
                    binding.todaysDateText.isGone = true
                    binding.tvClickButton.isGone = false
                }

                // AlertDialog
                if (!prefsManager.flagIsChecked && listHabits.size == theOnlyHabit) {
                    showAlertDialogKillHabit()
                    prefsManager.flagIsChecked = true
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
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
                val position = viewHolder.bindingAdapterPosition
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
                val backGroundColor = resources.getColor(R.color.transparent, null)
                val deleteDrawable =
                    getDrawable(requireContext(), R.drawable.icon_trashcan_red)
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
                    valueDp, valueDp, valueDp, valueDp, valueDp, valueDp, valueDp, valueDp
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

    private fun showAlertDialogInfoDetailed(habit: Habit) {
        val viewAlertDialogInfoDetailed: View =
            layoutInflater.inflate(R.layout.fragment_dialog_info_detailed, null, false)

        // надо эти if'ы оптимизировать, но голова не соображает
        if (habit.repeatDays.monday) {
            drawAlertDetail(viewAlertDialogInfoDetailed, R.id.progress_monday_t_v)
        }
        if (habit.repeatDays.thursday) {
            drawAlertDetail(viewAlertDialogInfoDetailed, R.id.progress_thursday_t_v)
        }
        if (habit.repeatDays.wednesday) {
            drawAlertDetail(viewAlertDialogInfoDetailed, R.id.progress_wednesday_t_v)
        }
        if (habit.repeatDays.tuesday) {
            drawAlertDetail(viewAlertDialogInfoDetailed, R.id.progress_tuesday_t_v)
        }
        if (habit.repeatDays.friday) {
            drawAlertDetail(viewAlertDialogInfoDetailed, R.id.progress_friday_t_v)
        }
        if (habit.repeatDays.saturday) {
            drawAlertDetail(viewAlertDialogInfoDetailed, R.id.progress_saturday_t_v)
        }
        if (habit.repeatDays.sunday) {
            drawAlertDetail(viewAlertDialogInfoDetailed, R.id.progress_sunday_t_v)
        }

        viewAlertDialogInfoDetailed.findViewById<TextView>(R.id.name_habit_dialog_tv).text = habit.name
        viewAlertDialogInfoDetailed.findViewById<ImageView>(R.id.image_habit).setImageResource(habit.urlImage)

        val alertDialog = AlertDialog
            .Builder(requireContext(), R.style.AlertDialogStyle)
            .setView(viewAlertDialogInfoDetailed)
            .show()

        viewAlertDialogInfoDetailed.findViewById<Button>(R.id.button_ok).setOnClickListener {
            alertDialog.dismiss()
        }

//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Заголовок")
//            .setMessage("Сообщение")
//            .setPositiveButton("OK", null)
//
//        val alertDialog2 = builder.create()
//
//        val window = alertDialog2.window
//        if (window != null) {
//            // Установка фона и прозрачности
//            window.setBackgroundDrawableResource(R.drawable.style_alert_dialog)
//            window.setDimAmount(0.3f) // Значение 0.5 обозначает 50% прозрачности, можете настроить по своему усмотрению
//        }
//
//        alertDialog2.show()
    }

    private fun drawAlertDetail(viewInfo: View, iconId: Int) {
        viewInfo
            .findViewById<TextView>(iconId)
            .setCompoundDrawablesWithIntrinsicBounds(
                getDrawable(requireContext(), R.drawable.icon_check_blue_fcbk),
                null,
                null,
                null
            )
    }

    companion object {
        const val theOnlyHabit = 1
    }
}
