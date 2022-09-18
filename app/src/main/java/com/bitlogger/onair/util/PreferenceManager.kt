package `in`.bitlogger.studentsolutions.utils

import android.content.Context
import android.content.SharedPreferences
import com.bitlogger.onair.util.Constants

class PreferenceManager(
    context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    /**
     * Never will be null because passing default string.
     * */
    fun getString(key: String): String {
        return sharedPreferences.getString(key, Constants.PREFERENCE_DEFAULT_STRING)!!
    }

    fun setInt(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, Constants.PREFERENCE_DEFAULT_INT)
    }

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    fun getBoolean(key: String): Boolean {
       return sharedPreferences.getBoolean(key, Constants.PREFERENCE_DEFAULT_BOOLEAN)
    }

    fun setDouble(key: String, value: Double) {
        sharedPreferences.edit()
            .putFloat(key, value.toFloat())
            .apply()
    }

    fun getDouble(key: String): Double {
        return sharedPreferences.getFloat(key, Constants.PREFERENCE_DEFAULT_FLOAT).toDouble()
    }
}