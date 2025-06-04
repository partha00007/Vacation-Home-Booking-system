/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This activity allows new users to register for an account in the Vacanza app.
 * It collects input (username, email, password), sends a registration request to the backend API,
 * and handles both success and error responses. The screen also allows users to navigate
 * to the login or home screen.
 */

package com.vacationhome.app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import android.graphics.Paint
import android.util.Log
import okhttp3.ResponseBody

/**
 * SignUpActivity:
 * Handles user registration via input form and Retrofit API call.
 */
class SignUpActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signupButton: Button
    private lateinit var closeButton: ImageView
    private lateinit var backToLoginText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Bind layout views
        usernameInput = findViewById(R.id.editUsername)
        emailInput = findViewById(R.id.editEmail)
        passwordInput = findViewById(R.id.editPassword)
        signupButton = findViewById(R.id.btnSignup)
        closeButton = findViewById(R.id.btnCloseSignup)
        backToLoginText = findViewById(R.id.backToLoginText)

        // Underline "Back to Login" text for clarity
        backToLoginText.paintFlags = backToLoginText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // Sign-up button click handler
        signupButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validate required fields
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Launch sign-up request in background
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val api = RetrofitClient.getInstance(this@SignUpActivity)

                    // Payload structure expected by FastAPI backend
                    val body = mapOf(
                        "username" to username,
                        "email" to email,
                        "password" to password,
                        "role" to "2", // default user role
                        "userName" to username,
                        "firstName" to "FirstName",
                        "lastName" to "LastName",
                        "mobileNo" to "0000000000"
                    )

                    val response = api.signup(body)

                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Sign-up successful! Please log in.",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        val errorBody: ResponseBody? = response.errorBody()
                        val errorMsg = errorBody?.string() ?: "Unknown error"
                        Log.e("API_SIGNUP_ERROR", "Signup failed: $errorMsg")

                        runOnUiThread {
                            if (errorMsg.contains("already exists", ignoreCase = true)) {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "User already exists. Please try logging in.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Sign-up failed. Please check your input.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                } catch (e: HttpException) {
                    Log.e("API_SIGNUP_ERROR", "HTTP error: ${e.code()}")
                    runOnUiThread {
                        Toast.makeText(this@SignUpActivity, "Server error", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("API_SIGNUP_ERROR", "Unexpected error: ${e.localizedMessage}")
                    runOnUiThread {
                        Toast.makeText(this@SignUpActivity, "Unexpected error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Close button navigates to home screen
        closeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // "Back to Login" text navigates to login screen
        backToLoginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
