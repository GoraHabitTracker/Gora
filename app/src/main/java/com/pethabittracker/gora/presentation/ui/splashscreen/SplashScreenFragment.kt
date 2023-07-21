package com.pethabittracker.gora.presentation.ui.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pethabittracker.gora.R
import com.pethabittracker.gora.databinding.FragmentSplashscreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashscreenBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSplashscreenBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            iconIv.alpha = 0.0f
            iconIv.animate()
                .setDuration(2500)
                .alpha(1.0f)
                .startDelay = 1000

            titleTv.alpha = 0.0f
            titleTv.animate()
                .setDuration(1500)
                .alpha(1.0f)
                .startDelay = 3500

            viewLifecycleOwner.lifecycleScope.launch {
                delay(6000)
                findNavController().navigate(R.id.action_global_splashscreen)

            }

            iconIv.setOnClickListener {
                findNavController().navigate(R.id.action_global_splashscreen)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}