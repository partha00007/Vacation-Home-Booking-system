/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This object sets up and provides a singleton Retrofit client instance
 * for making HTTP requests to the backend API. It includes a method to create an
 * implementation of the ApiService interface using the defined base URL.
 */

package com.vacationhome.app

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object for initializing Retrofit with a base URL and HTTP client.
 */
object RetrofitClient {

    // Base URL for the backend API
    private const val BASE_URL = "http://10.0.2.2:8000/"

    // Retrofit instance (lazy initialized)
    private var retrofit: Retrofit? = null

    /**
     * Returns a singleton instance of ApiService.
     * Automatically adds Authorization header if user is logged in.
     */
    fun getInstance(context: Context): ApiService {
        if (retrofit == null) {
            // Token interceptor
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

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!.create(ApiService::class.java)
    }
}
