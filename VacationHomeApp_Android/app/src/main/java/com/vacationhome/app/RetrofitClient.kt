/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This object sets up and provides a singleton Retrofit client instance
 * for making HTTP requests to the backend API. It uses an OkHttp interceptor
 * to attach an Authorization header when a JWT token is available.
 */

package com.vacationhome.app

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitClient:
 * Singleton object responsible for initializing and providing access
 * to the Retrofit API interface. Adds authentication token if available.
 */
object RetrofitClient {

    // Backend API base URL (local development - use 10.0.2.2 for emulator access)
     private const val BASE_URL = "http://10.0.2.2:8000/api/"

    // Retrofit instance, lazily initialized
    private var retrofit: Retrofit? = null

    /**
     * Returns an instance of ApiService.
     * Adds Authorization header if a token exists in SharedPreferences.
     */
    fun getInstance(context: Context): ApiService {
        if (retrofit == null) {

            // Interceptor to append token to each request if available
            val authInterceptor = Interceptor { chain ->
                val originalRequest: Request = chain.request()
                val prefs = context.getSharedPreferences("VacanzaPrefs", Context.MODE_PRIVATE)
                val token = prefs.getString("token", null)

                val newRequest = if (!token.isNullOrEmpty()) {
                    originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    originalRequest
                }

                chain.proceed(newRequest)
            }

            // Build OkHttpClient with the token interceptor
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            // Build Retrofit instance with base URL and Gson parser
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        // Create and return an implementation of ApiService
        return retrofit!!.create(ApiService::class.java)
    }
}
