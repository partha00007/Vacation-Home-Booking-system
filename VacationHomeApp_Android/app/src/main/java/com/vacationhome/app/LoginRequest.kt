/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This data class defines the structure of the login request
 * payload sent to the backend API. It includes the user's username and password.
 */

package com.vacationhome.app.models

/**
 * Represents the body of a login request sent to the backend.
 * Used for authenticating users.
 */
data class LoginRequest(
    val userName: String,
    val password: String
)
