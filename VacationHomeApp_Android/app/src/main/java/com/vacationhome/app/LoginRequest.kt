/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This data class defines the structure of the login request payload
 * sent to the backend API. It includes two fields: userName and password.
 * The backend uses this object to authenticate the user's credentials.
 */

package com.vacationhome.app.models

/**
 * LoginRequest:
 * Represents the JSON body sent when performing user login.
 */
data class LoginRequest(
    val userName: String,
    val password: String
)
