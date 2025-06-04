/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This data class represents the response returned by the backend API
 * after a login attempt. It includes a message (e.g., success or failure) and an optional
 * token (JWT) used for authenticated requests if login is successful.
 */

package com.vacationhome.app.models

/**
 * Model for the login response returned from the server.
 *
 * @param message A status message describing the outcome.
 * @param token Optional JWT token if authentication is successful.
 */
data class LoginResponse(
    val message: String,
    val token: String? = null
)
