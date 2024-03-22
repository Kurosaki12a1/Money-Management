package com.kuro.money.data.data_source.local.preferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppPreferences @Inject constructor(val context: Context) {
    companion object {
        private const val APP_PREFERENCES_NAME = "Money-Cache"
        private const val MODE = Context.MODE_PRIVATE

        private const val ONBOARDING = "onboarding"
        private const val IS_FIRST_TIME = "is_first_time_open_app"

    }

    private val appPreferences: SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES_NAME, MODE)

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isOnBoardingDone: Boolean
        get() = appPreferences.getBoolean(ONBOARDING, false)
        set(value) = appPreferences.edit {
            it.putBoolean(ONBOARDING, value)
        }

    var isFirstTimeOpenApp : Boolean
        get() = appPreferences.getBoolean(IS_FIRST_TIME, true)
        set(value) = appPreferences.edit {
            it.putBoolean(IS_FIRST_TIME, value)
        }

    fun clearPreferences() {
        appPreferences.edit {
            it.clear().apply()
        }
    }
}