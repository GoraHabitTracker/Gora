package com.pethabittracker.gora.presentation.ui.newhabits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.FragmentNewHabitBinding
import com.pethabittracker.gora.domain.models.WeekList
import com.pethabittracker.gora.presentation.models.Priority
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
                selectedIcon.setImageResource(R.drawable.girl_runs_png)
                // приравниваем текущую иконку к новой(выбранной)
                currentIconHabit = selectedIcon
            }
        }

        with(binding) {
            toolbarDetail.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            emoji1.setOnClickListener {
                if (currentIconHabit == emoji1) {
                    // выделяем иконку
                    emoji1.setImageResource(R.drawable.girl_runs_png)
                } else {
                    selectTheIcon(emoji1)
                    urlImage = R.drawable.spanch
                }
            }
            emoji2.setOnClickListener {
                selectTheIcon(emoji2)
                urlImage = R.drawable.spanch_2
            }
            emoji3.setOnClickListener {
                selectTheIcon(emoji3)
                urlImage = R.drawable.spanch_3
            }
            emoji4.setOnClickListener {
                selectTheIcon(emoji4)
                urlImage = R.drawable.spanch_4
            }
            emoji5.setOnClickListener {
                selectTheIcon(emoji5)
                urlImage = R.drawable.spanch_5
            }
            emoji6.setOnClickListener {
                selectTheIcon(emoji6)
                urlImage = R.drawable.spanch_6
            }
            emoji7.setOnClickListener {
                selectTheIcon(emoji7)
                urlImage = R.drawable.spanch_7
            }
            emoji8.setOnClickListener {
                selectTheIcon(emoji8)
                urlImage = R.drawable.spanch_8
            }
            emoji9.setOnClickListener {
                selectTheIcon(emoji9)
                urlImage = R.drawable.spanch_9
            }
            emoji10.setOnClickListener {
                selectTheIcon(emoji10)
                urlImage = R.drawable.spanch_10
            }

            buttonSave.setOnClickListener { view ->
                val titleHabit = containerTitle.getTextOrSetError() ?: return@setOnClickListener

                val monday = monday.isChecked
                val thursday = thursday.isChecked
                val wednesday = wednesday.isChecked
                val tuesday = tuesday.isChecked
                val friday = friday.isChecked
                val saturday = saturday.isChecked
                val sunday = sunday.isChecked

                if (!monday && !thursday && !wednesday && !tuesday && !friday && !saturday && !sunday) {
                    val snackbar = Snackbar.make(
                        view,
                        "Выберите хотя бы один день повторения привычки",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.show()
                    return@setOnClickListener
                }

                val selectedDays =
                    WeekList(monday, thursday, wednesday, tuesday, friday, saturday, sunday)

                lifecycleScope.launch {
                    runCatching {
                        val newHabit = viewModel.newHabit(
                            titleHabit,
                            urlImage,
                            Priority.Default.value,
                            selectedDays
                        )
                        viewModel.insertHabit(newHabit)
                    }
                }

                viewModel
                    .checkSingleIdOne()
                    .onEach {
                        if (it) showAlertDialog()
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)

                findNavController().navigateUp()
                // TODO надо убрать клаву с выходом из этого фрагмента
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

    private fun showAlertDialog() {
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

    companion object {
        const val theOnlyHabit = 1
    }
}
