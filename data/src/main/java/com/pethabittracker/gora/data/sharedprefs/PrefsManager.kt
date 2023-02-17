package com.pethabittracker.gora.data.sharedprefs
import android.content.Context
import androidx.core.content.edit
import com.pethabittracker.gora.data.R

class PrefsManager(context: Context) {

    private val prefs = context.getSharedPreferences(R.string.prefs_name.toString(), Context.MODE_PRIVATE)

    var flagIsChecked: Boolean
        get() = prefs.getBoolean(R.string.key_flag.toString(), false)
        set(value) {
            prefs.edit {
                putBoolean(R.string.key_flag.toString(), value)
            }
        }
}
