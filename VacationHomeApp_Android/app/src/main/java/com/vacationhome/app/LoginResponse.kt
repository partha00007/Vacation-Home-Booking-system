/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This data class represents the response returned by the backend API
 * after a login attempt. It contains a message indicating the outcome (success/failure)
 * and optionally a JWT token used for authenticated requests.
 */

package com.vacationhome.app.models

/**
 * LoginResponse:
 * Represents the server's response to a login request.
 *
 * @param message A human-readable status message.
 * @param token A JWT token (if login is successful); null otherwise.
 */
data class LoginResponse(
    val message: String,
    val token: String? = null
)
