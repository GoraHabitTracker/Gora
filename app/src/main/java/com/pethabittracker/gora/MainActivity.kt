package com.pethabittracker.gora

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(R.layout.activity_main)

    // ---------------- Код для заставки (SplashScreen) -----------------
// в Манифесте в активити надо добавить  android:theme="@style/SplashScreen" и в файл gradle вписать зависимости

//class MainActivity : AppCompatActivity(R.layout.activity_main) {
//    override fun onCreate(saveInstanceState: Bundle?) {
//        installSplashScreen()
//        super.onCreate(saveInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}
