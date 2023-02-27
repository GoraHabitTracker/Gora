package com.pethabittracker.gora.presentation.ui.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.pethabittracker.gora.NavigationDirections
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.FragmentContentBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContentFragment : Fragment() {

    private var _binding: FragmentContentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel by viewModel<ContentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentContentBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedController =
            (childFragmentManager
                .findFragmentById(R.id.container_content) as NavHostFragment)
                .navController

        with(binding) {
            bottomNavigation.setupWithNavController(nestedController)

            // Ограничение на 10 привычек
            viewModel.countHabitFlow
                .onEach {
                    if (it < allowedCountOfHabit) {
                        fab.setImageResource(R.drawable.icon_button_add)
                        fab.setOnClickListener {
                            findNavController().navigate(NavigationDirections.actionGlobalFab())
                        }
                    } else {
                        fab.apply {
                            setImageResource(R.drawable.icon_button_add_negative)
                            setColorFilter(R.color.periwinkle)
                            setBackgroundColor(resources.getColor(R.color.cross_background, null))
                            setOnClickListener {
                                showAlertDialog()
                                val toast = Toast.makeText(
                                    context,
                                    R.string.limit_habit,
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                            }
                        }
                    }
                }.launchIn(lifecycleScope)

            // отключаем кликабельность средней кнопки
            bottomNavigation.menu.getItem(1).isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAlertDialog() {
        val viewAlertDialog =
            layoutInflater.inflate(R.layout.fragment_dialog_habits_limit, null, false)
        val alertDialog = AlertDialog
            .Builder(requireContext(), R.style.AlertDialogStyle)
            .setView(viewAlertDialog)
            .show()
        viewAlertDialog.findViewById<Button>(R.id.button_i_see).setOnClickListener {
            alertDialog.dismiss()
        }
    }

    companion object {
        const val allowedCountOfHabit = 10
    }
}
