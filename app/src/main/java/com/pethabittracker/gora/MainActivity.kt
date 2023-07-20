package com.pethabittracker.gora

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(saveInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(saveInstanceState)

//        binding.iv.alpha = 0.0f
//        binding.iv.animate()
//            .setDuration(3000)
//            .alpha(1.0F)
//            .startDelay = 1000

        //----------------------------------------------------------------------------------------
//        Thread.sleep(2000)
//        installSplashScreen()
        setContentView(R.layout.activity_main)
        //----------------------------------------------------------------------------------------

        //----------------------------------------------------------------------------------------
//        splashScreen.setOnExitAnimationListener { splashScreenView ->
//            val slideUp = ObjectAnimator.ofFloat(
//                splashScreenView,
//                View.TRANSLATION_Y,
//                0.0f,
//                -splashScreenView.height.toFloat()
//            )
//            slideUp.interpolator = AnticipateInterpolator()
//            slideUp.duration = 3000L
//            slideUp.doOnEnd { splashScreenView.remove() }
//            slideUp.start()
//        }
//        setContentView(R.layout.activity_main)
        //----------------------------------------------------------------------------------------

        //----------------------------------------------------------------------------------------
//        splashScreen.setOnExitAnimationListener { splashScreenView ->
//            val slideUp = ObjectAnimator.ofFloat(
//                splashScreenView,
//                View.ALPHA,
//                0.0f,
//                1.0f
//            )
//            slideUp.duration = 4000L
//            slideUp.doOnEnd { splashScreenView.remove() }
//            slideUp.start()
//        }
//        setContentView(R.layout.activity_main)
        //----------------------------------------------------------------------------------------

        //----------------------------------------------------------------------------------------
//        val view = R.layout.activity_main
//        setContentView(R.layout.activity_main)
//        View.ALPHA.set(splashScreen, 0f)
    }
}
