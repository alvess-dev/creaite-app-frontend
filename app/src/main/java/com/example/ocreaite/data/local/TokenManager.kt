package com.example.ocreaite.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenManager(context: Context) {

    private val TAG = "TokenManager"
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "auth_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
    }

    fun saveToken(accessToken: String) {
        Log.d(TAG, "Saving token")
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            apply()
        }
        Log.d(TAG, "✅ Token saved successfully")
    }

    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun clearTokens() {
        Log.d(TAG, "Clearing token")
        prefs.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            apply()
        }
        Log.d(TAG, "✅ Token cleared")
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }
}