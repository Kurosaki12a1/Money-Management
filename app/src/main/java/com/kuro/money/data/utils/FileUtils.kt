package com.kuro.money.data.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object FileUtils {
    inline fun <reified T> loadJsonFromAsset(context: Context, fileName: String): T? {
        val jsonString: String = try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }

        return Gson().fromJson(jsonString, object : TypeToken<T>() {}.type)
    }
}