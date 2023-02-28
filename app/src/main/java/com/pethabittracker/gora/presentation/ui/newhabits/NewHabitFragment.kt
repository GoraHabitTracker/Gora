package com.pethabittracker.gora.presentation.ui.newhabits

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.pethabittracker.gora.R
import com.pethabittracker.gora.data.utils.setBackgrndColor
import com.pethabittracker.gora.databinding.FragmentNewHabitBinding
import com.pethabittracker.gora.domain.models.WeekList
import com.pethabittracker.gora.presentation.models.Priority
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

        var currentIconHabit: ShapeableImageView = binding.emoji1
        var urlImage = R.drawable.spanch
        fun selectTheIcon(selectedIcon: ShapeableImageView) {
            if (selectedIcon != currentIconHabit) {
                // снимаем выделения с currentIconHabit
                currentIconHabit.setImageResource(R.drawable.spanch)
                // выделяем новую выбранную иконку
                selectedIcon.setImageResource(R.drawable.image_girl_runs_png)
                // приравниваем текущую иконку к новой(выбранной)
                currentIconHabit = selectedIcon
            }
        }

        with(binding) {
            toolbarDetail.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            var monday = mondayBttn.isChecked
            var thursday = thursdayBttn.isChecked
            var wednesday = wednesdayBttn.isChecked
            var tuesday = tuesdayBttn.isChecked
            var friday = fridayBttn.isChecked
            var saturday = saturdayBttn.isChecked
            var sunday = sundayBttn.isChecked

            val selectedDays =
                WeekList(monday, thursday, wednesday, tuesday, friday, saturday, sunday)

            mondayBttn.setOnClickListener {
                selectedDays.monday = mondayBttn.isChecked
                monday = mondayBttn.isChecked
                viewModel.onChangeDays(selectedDays)
            }
            thursdayBttn.setOnClickListener {
                selectedDays.thursday = thursdayBttn.isChecked
                thursday = thursdayBttn.isChecked
                viewModel.onChangeDays(selectedDays)
            }
            wednesdayBttn.setOnClickListener {
                selectedDays.wednesday = wednesdayBttn.isChecked
                wednesday = wednesdayBttn.isChecked
                viewModel.onChangeDays(selectedDays)
            }
            tuesdayBttn.setOnClickListener {
                selectedDays.tuesday = tuesdayBttn.isChecked
                tuesday = tuesdayBttn.isChecked
                viewModel.onChangeDays(selectedDays)
            }
            fridayBttn.setOnClickListener {
                selectedDays.friday = fridayBttn.isChecked
                friday = fridayBttn.isChecked
                viewModel.onChangeDays(selectedDays)
            }
            saturdayBttn.setOnClickListener {
                selectedDays.saturday = saturdayBttn.isChecked
                saturday = saturdayBttn.isChecked
                viewModel.onChangeDays(selectedDays)
            }
            sundayBttn.setOnClickListener {
                selectedDays.sunday = sundayBttn.isChecked
                sunday = sundayBttn.isChecked
                viewModel.onChangeDays(selectedDays)
            }

            emoji1.setOnClickListener {
                viewModel.onChangeIcon()
                if (currentIconHabit == emoji1) {
                    // выделяем иконку
                    emoji1.setImageResource(R.drawable.image_girl_runs_png)
                } else {
                    selectTheIcon(emoji1)
                    urlImage = R.drawable.spanch
                }
            }
            emoji2.setOnClickListener {
                selectTheIcon(emoji2)
                viewModel.onChangeIcon()
                urlImage = R.drawable.spanch_2
            }
            emoji3.setOnClickListener {
                viewModel.onChangeIcon()
                selectTheIcon(emoji3)
                urlImage = R.drawable.spanch_3
            }
            emoji4.setOnClickListener {
                viewModel.onChangeIcon()
                selectTheIcon(emoji4)
                urlImage = R.drawable.spanch_4
            }
            emoji5.setOnClickListener {
                viewModel.onChangeIcon()
                selectTheIcon(emoji5)
                urlImage = R.drawable.spanch_5
            }
            emoji6.setOnClickListener {
                viewModel.onChangeIcon()
                selectTheIcon(emoji6)
                urlImage = R.drawable.spanch_6
            }
            emoji7.setOnClickListener {
                viewModel.onChangeIcon()
                selectTheIcon(emoji7)
                urlImage = R.drawable.spanch_7
            }
            emoji8.setOnClickListener {
                viewModel.onChangeIcon()
                selectTheIcon(emoji8)
                urlImage = R.drawable.spanch_8
            }
            emoji9.setOnClickListener {
                viewModel.onChangeIcon()
                selectTheIcon(emoji9)
                urlImage = R.drawable.spanch_9
            }
            emoji10.setOnClickListener {
                viewModel.onChangeIcon()
                selectTheIcon(emoji10)
                urlImage = R.drawable.spanch_10
            }

            buttonSave.setOnClickListener { view ->
                val titleHabit = containerTitle.getTextOrSetError() ?: return@setOnClickListener

                if (!monday && !thursday && !wednesday && !tuesday && !friday && !saturday && !sunday) {
                    alarmTv.text = getString(R.string.alarm_selection_day)
                    return@setOnClickListener
                }
                if (urlImage == R.drawable.spanch) {
                    alarmTv.text = getString(R.string.alarm_selection_icon)
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    runCatching {
                        viewModel.newHabit(
                            titleHabit,
                            urlImage,
                            Priority.Default.value,
                            selectedDays
                        )
                    }
                }

                findNavController().navigateUp()
            }

            // убираем клаву при переходе на другой фрагмент
            editTextTitle.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    val imm =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }

            viewModel.isAllowedSave
                .onEach {
                    buttonSave.setBackgrndColor(R.color.periwinkle)
                }
                .filter { it }
                .onEach {
                    buttonSave.setBackgrndColor(R.color.blue_fcbk)
                }
                .launchIn(lifecycleScope)

            editTextTitle.doOnTextChanged { text, _, _, _ ->
                if (text?.length != 0) {
                    viewModel.onChangeTitle(true)
                } else {
                    viewModel.onChangeTitle(false)
                }
            }

            // очищаем поле ввода при нажатии на иконку "х"
            containerTitle.setEndIconOnClickListener {
                editTextTitle.setText("")
                viewModel.onChangeTitle(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun TextInputLayout.getTextOrSetError(): String? {
        return editText?.text?.toString()?.ifBlank {
            with(binding) {
                error = getString(R.string.empty_field)
                hintTextColor = ContextCompat.getColorStateList(context, R.color.pastel_red)
                editTextTitle.background = ResourcesCompat.getDrawable(
                    resources, R.drawable.border_edittext_error, null
                )
                editTextTitle.doOnTextChanged { text, _, _, _ ->
                    if (text?.length != 0) {
                        error = null
                        editTextTitle.background = ResourcesCompat.getDrawable(
                            resources, R.drawable.background_button_skip, null
                        )
                        hintTextColor = ContextCompat.getColorStateList(context, R.color.periwinkle)
                    }
                }

                null
            }
        }
    }
}
