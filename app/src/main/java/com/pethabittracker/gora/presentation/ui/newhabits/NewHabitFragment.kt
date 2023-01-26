package com.pethabittracker.gora.presentation.ui.newhabits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.pethabittracker.gora.databinding.FragmentNewHabitBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewHabitFragment : Fragment() {

    private var _binding: FragmentNewHabitBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val newHabitViewModel by viewModel<NewHabitViewModel>()

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

        with(binding) {
            toolbarDetail.setNavigationOnClickListener{
                findNavController().navigateUp()
            }

            buttonSave.setOnClickListener {
                val titleHabit = containerTitle.getTextOrSetError() ?: return@setOnClickListener

                lifecycleScope.launch {
                    runCatching {
                        val newHabit = newHabitViewModel.newHabit(titleHabit, "url", 1)
                        newHabitViewModel.insertHabit(newHabit)
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
                error = "Field is empty"
                null
            }
    }
}
