package com.example.ocreaite.utils

import com.example.ocreaite.BuildConfig
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class GoogleSignInHelper(private val context: Context) {

    private val googleSignInClient: GoogleSignInClient
    private val TAG = "GoogleSignInHelper"

    init {
        Log.d(TAG, "=== Initializing Google Sign In ===")
        Log.d(TAG, "Package name: ${context.packageName}")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
        Log.d(TAG, "GoogleSignInClient created successfully")
    }

    fun getSignInIntent(): Intent {
        Log.d(TAG, "=== Getting Sign In Intent ===")
        val intent = googleSignInClient.signInIntent
        Log.d(TAG, "Sign In Intent created")
        return intent
    }

    fun handleSignInResult(data: Intent?): String? {
        Log.d(TAG, "=== Handling Sign In Result ===")

        if (data == null) {
            Log.e(TAG, "Intent data is NULL")
            return null
        }

        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

        return try {
            val account = task.getResult(ApiException::class.java)
            Log.d(TAG, "✅ Sign in SUCCESSFUL")
            Log.d(TAG, "Email: ${account.email}")
            Log.d(TAG, "Name: ${account.displayName}")
            Log.d(TAG, "ID Token present: ${account.idToken != null}")
            Log.d(TAG, "ID Token (first 50 chars): ${account.idToken?.take(50)}")
            account.idToken
        } catch (e: ApiException) {
            Log.e(TAG, "❌ Sign in FAILED")
            Log.e(TAG, "Status Code: ${e.statusCode}")
            Log.e(TAG, "Status Message: ${e.message}")
            Log.e(TAG, "Full error: ${e.toString()}")

            when (e.statusCode) {
                10 -> Log.e(TAG, "DEVELOPER_ERROR: Check SHA-1 certificate and package name")
                12500 -> Log.e(TAG, "SIGN_IN_CANCELLED: User cancelled the sign-in")
                12501 -> Log.e(TAG, "SIGN_IN_CURRENTLY_IN_PROGRESS")
                7 -> Log.e(TAG, "NETWORK_ERROR: Check internet connection")
                else -> Log.e(TAG, "UNKNOWN ERROR CODE")
            }

            null
        }
    }

    fun signOut() {
        googleSignInClient.signOut()
        Log.d(TAG, "User signed out")
    }
}