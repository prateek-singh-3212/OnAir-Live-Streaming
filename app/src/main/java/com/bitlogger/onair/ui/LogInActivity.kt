package com.bitlogger.onair.ui

import `in`.bitlogger.studentsolutions.utils.PreferenceManager
import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bitlogger.onair.databinding.ActivityLogInBinding
import com.bitlogger.onair.databinding.ActivityMainBinding
import com.bitlogger.onair.util.Constants
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(preferenceManager.getBoolean(Constants.LOGIN_STATUS)) {
            home()
        }

        mGoogleSignInClient = GoogleSignIn.getClient(this, getGoogleSignInOptions())

        binding.signInButton.setOnClickListener {
            // Initialize sign in intent
            val intent= mGoogleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent,100);
        }

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()

        if (firebaseUser != null) {
            // When user already sign in
            home()
        }
    }

    private fun home() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 100) {
            // When request code is equal to 100
            // Initialize task
            val signInAccountTask = GoogleSignIn
                .getSignedInAccountFromIntent(data)

            // check condition
            if (signInAccountTask.isSuccessful) {
                // When google sign in successful
                // Initialize string
                val s = "Google sign in successful"
                // Display Toast
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    val googleSignInAccount = signInAccountTask
                        .getResult(ApiException::class.java)
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null
                        // Initialize auth credential
                        val authCredential = GoogleAuthProvider
                            .getCredential(
                                googleSignInAccount.idToken, null
                            )
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                            .addOnCompleteListener(
                                this
                            ) { task ->
                                // Check condition
                                if (task.isSuccessful) {
                                    // When task is successful
                                    // Redirect to profile activity
                                    home()
                                    // Display Toast
                                    Log.d("SS", "Firebase authentication successful")
                                } else {
                                    // When task is unsuccessful
                                    // Display Toast
                                    Log.d("SS", "Authentication Failed :")
                                }
                            }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.G_CLIENT_ID)
            .requestEmail()
            .build()
    }
}