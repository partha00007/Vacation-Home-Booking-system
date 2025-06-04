/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This activity allows new users to register for an account in the Vacanza app.
 * It collects user input (username, email, password), sends a sign-up request to the backend API,
 * and handles success or failure responses. It also provides navigation to the login and home screens.
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
 * Handles user registration via form input and backend API call.
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

        // Bind view components
        usernameInput = findViewById(R.id.editUsername)
        emailInput = findViewById(R.id.editEmail)
        passwordInput = findViewById(R.id.editPassword)
        signupButton = findViewById(R.id.btnSignup)
        closeButton = findViewById(R.id.btnCloseSignup)
        backToLoginText = findViewById(R.id.backToLoginText)

        // Underline the "Back to Login" text
        backToLoginText.paintFlags = backToLoginText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // Handle sign-up button click
        signupButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Basic validation
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val api = RetrofitClient.getInstance(this@SignUpActivity)

                    // Construct sign-up body as expected by FastAPI backend
                    val body = mapOf(
                        "username" to username,
                        "email" to email,
                        "password" to password,
                        "role" to "2", // default role (user)
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

        // Close and return to home screen
        closeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Navigate back to login screen
        backToLoginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
