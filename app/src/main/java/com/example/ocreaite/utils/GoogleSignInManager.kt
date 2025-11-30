package com.example.ocreaite.utils

import android.content.Context
import android.util.Log

object GoogleSignInManager {
    private var helper: GoogleSignInHelper? = null
    private const val TAG = "GoogleSignInManager"

    fun initialize(context: Context) {
        Log.d(TAG, "=== Initializing GoogleSignInManager ===")
        if (helper == null) {
            helper = GoogleSignInHelper(context.applicationContext)
            Log.d(TAG, "✅ GoogleSignInHelper created")
        } else {
            Log.d(TAG, "⚠️ GoogleSignInHelper already initialized")
        }
    }

    fun getHelper(): GoogleSignInHelper {
        Log.d(TAG, "Getting helper, is null: ${helper == null}")
        return helper ?: throw IllegalStateException("GoogleSignInManager not initialized! Call initialize() first.")
    }

    fun isInitialized(): Boolean = helper != null
}