/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This activity allows users to log into the Vacanza app. It handles user input,
 * performs login authentication using a backend API, and manages session state via SharedPreferences.
 * It also provides navigation to the home screen and the sign-up screen.
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
 * LoginActivity handles user login and session persistence.
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

        sharedPreferences = getSharedPreferences("VacanzaPrefs", MODE_PRIVATE)

        // Bind views to layout elements
        emailInput = findViewById(R.id.editUsername)
        passwordInput = findViewById(R.id.editPassword)
        loginButton = findViewById(R.id.btnLogin)
        signUpButton = findViewById(R.id.btnSignUp)
        closeButton = findViewById(R.id.btnCloseSignup)

        // Handle login button click
        loginButton.setOnClickListener {
            val username = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validate input
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Perform login request using coroutine
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val api = RetrofitClient.getInstance(this@LoginActivity)
                    val credentials = mapOf("userName" to username, "password" to password)

                    val response = api.login(credentials)

                    if (response.isSuccessful && response.body() != null) {
                        // Store login state
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

        // Close button takes user to HomeActivity
        closeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Sign-up button takes user to SignUpActivity
        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

        }
    }
}
