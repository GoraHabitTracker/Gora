package com.pethabittracker.gora.presentation.ui.home

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.FragmentHomeBinding
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
            onButtonDoneClicked = {
                viewModel.skipDown(it)
            }
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

            // AlertDialog повесил пока что на Фотку
            binding.foto.setOnClickListener {
                val viewAlertDialog =
                    layoutInflater.inflate(R.layout.fragment_dialog_deleting, null, false)
                val alertDialog = AlertDialog
                    .Builder(requireContext(), R.style.MyAlertTheme)
                    .setView(viewAlertDialog)
                    .show()
                viewAlertDialog.findViewById<Button>(R.id.button_gotit).setOnClickListener {
                    alertDialog.dismiss()
                }
            }
        }

        updateList()

        setSwipeToDelete()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateList() {

        //------------------ with Coroutine -------------------------------------------------------
        viewModel.getAllHabit().onEach {
            adapter.submitList(it)


        }.launchIn(lifecycleScope)
        viewModel
            .getAllHabit()
            .onEach {
                adapter.submitList(it)

                if (it.isNotEmpty()) {
                    binding.root.setBackgroundResource(R.color.sea_foam)
                } else {
                    binding.root.setBackgroundResource(R.color.transparent)
                }

                //для плавности замены слоёв
                delay(300)
                binding.foto.isVisible = it.isEmpty()
            }
            .launchIn(lifecycleScope)

        //------------------ with LiveData -------------------------------------------------------
//        viewModel.allHabit.observe(this.viewLifecycleOwner) { items ->
//            items.let { list ->
//                adapter.submitList(list)
//            }
//        }
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

                val mBackGround = ColorDrawable()
                val backGroundColor = Color.parseColor("#b80f0a")
                val deleteDrawable =
                    getDrawable(requireContext(), R.drawable.trashcan)
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

                mBackGround.color = backGroundColor
                mBackGround.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
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
}
