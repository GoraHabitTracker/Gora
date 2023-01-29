package com.pethabittracker.gora.presentation.ui.newhabits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.FragmentNewHabitBinding
import com.pethabittracker.gora.domain.models.WeekList
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewHabitFragment : Fragment() {

    private var _binding: FragmentNewHabitBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel by viewModel<NewHabitViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentNewHabitBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var imageHabit: ShapeableImageView = binding.emoji1
        var urlImage = R.drawable.spanch
        fun changeImage(currentImageHabit: ShapeableImageView) {
            if (currentImageHabit != imageHabit) {
                // снимаем выделения с imageHabit
                imageHabit.setImageResource(R.drawable.spanch)
                // выделяем текущую ImageView
                currentImageHabit.setImageResource(R.drawable.girl_runs_png)
                // приравниваем текущую ImageView к imageHabit
                imageHabit = currentImageHabit
            }
        }

        with(binding) {
            toolbarDetail.setNavigationOnClickListener {
                findNavController().navigateUp()
            }


            emoji1.setOnClickListener {
                changeImage(emoji1)
                urlImage = R.drawable.spanch
            }
            emoji2.setOnClickListener {
                changeImage(emoji2)
                urlImage = R.drawable.spanch_2
            }
            emoji3.setOnClickListener {
                changeImage(emoji3)
                urlImage = R.drawable.spanch_3
            }
            emoji4.setOnClickListener {
                changeImage(emoji4)
                urlImage = R.drawable.spanch_4
            }
            emoji5.setOnClickListener {
                changeImage(emoji5)
                urlImage = R.drawable.spanch_5
            }
            emoji6.setOnClickListener {
                changeImage(emoji6)
                urlImage = R.drawable.spanch_6
            }
            emoji7.setOnClickListener {
                changeImage(emoji7)
                urlImage = R.drawable.spanch_7
            }
            emoji8.setOnClickListener {
                changeImage(emoji8)
                urlImage = R.drawable.spanch_8
            }
            emoji9.setOnClickListener {
                changeImage(emoji9)
                urlImage = R.drawable.spanch_9
            }
            emoji10.setOnClickListener {
                changeImage(emoji10)
                urlImage = R.drawable.spanch_10
            }

            buttonSave.setOnClickListener {
                val titleHabit = containerTitle.getTextOrSetError() ?: return@setOnClickListener

                val monday = monday.isChecked
                val thursday = thursday.isChecked
                val wednesday = wednesday.isChecked
                val tuesday = tuesday.isChecked
                val friday = friday.isChecked
                val saturday = saturday.isChecked
                val sunday = sunday.isChecked

                if (!monday&&!thursday&&!wednesday&&!tuesday&&!friday&&!saturday&&!sunday){
                    val snackbar = Snackbar.make(it,"Выберите хотя бы один день повторения привычки",Snackbar.LENGTH_LONG)
                    snackbar.show()
                    return@setOnClickListener
                }

                val selectedDays =
                    WeekList(monday, thursday, wednesday, tuesday, friday, saturday, sunday)

                lifecycleScope.launch {
                    runCatching {

                        val newHabit = viewModel.newHabit(titleHabit, urlImage, 0, selectedDays)
                        viewModel.insertHabit(newHabit)

                    }
                }

                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun TextInputLayout.getTextOrSetError(): String? {
        return editText?.text?.toString()
            ?.ifBlank {
                error = getString(R.string.empty_field)
                null
            }
    }
}
