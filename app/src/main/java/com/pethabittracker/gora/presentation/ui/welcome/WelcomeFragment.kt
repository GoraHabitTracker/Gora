package com.pethabittracker.gora.presentation.ui.welcome

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.FragmentWelcomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel by viewModel<WelcomeFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentWelcomeBinding.inflate(inflater,container,false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            iv.alpha = 0.0f
            iv.animate()
                .setDuration(1500)
                .alpha(1.0F)
                .startDelay = 500

            textViewBeCreator.setOnClickListener {
                TransitionManager.beginDelayedTransition(root)
                iv.isVisible = !iv.isVisible
            }

            button.setOnClickListener {
                findNavController().navigate(R.id.action_global_welcome)
            }
        }

        viewModel.updateDaily()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
