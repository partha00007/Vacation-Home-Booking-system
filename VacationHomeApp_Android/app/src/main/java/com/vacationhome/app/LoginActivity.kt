/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This activity allows users to log into the Vacanza app.
 * It handles user input, performs backend authentication, manages session state using SharedPreferences,
 * and provides navigation to the Home screen and Sign-Up screen.
 */

package com.vacationhome.app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * LoginActivity:
 * Handles user login and session persistence with SharedPreferences.
 * Communicates with the backend API to authenticate user credentials.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var closeButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("VacanzaPrefs", MODE_PRIVATE)

        // Bind UI elements
        emailInput = findViewById(R.id.editUsername)
        passwordInput = findViewById(R.id.editPassword)
        loginButton = findViewById(R.id.btnLogin)
        signUpButton = findViewById(R.id.btnSignUp)
        closeButton = findViewById(R.id.btnCloseSignup)

        // Login button logic
        loginButton.setOnClickListener {
            val username = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validate fields
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Make login API call
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val api = RetrofitClient.getInstance(this@LoginActivity)
                    val credentials = mapOf("userName" to username, "password" to password)

                    val response = api.login(credentials)

                    if (response.isSuccessful && response.body() != null) {
                        // Save login session
                        sharedPreferences.edit {
                            putBoolean("isLoggedIn", true)
                            putString("username", username)
                        }

                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Login failed. Check credentials.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: HttpException) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "HTTP error: ${e.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Unexpected error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Close icon: return to HomeActivity
        closeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Sign-up button: open SignUpActivity
        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
