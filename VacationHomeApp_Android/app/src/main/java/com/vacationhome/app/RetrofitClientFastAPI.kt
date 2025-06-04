/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This object initializes a Retrofit client configured to communicate
 * with a FastAPI backend server. It uses a custom CookieJar to handle session
 * cookies manually (useful for Django/CSRF/session-based auth).
 * The singleton pattern ensures only one instance of Retrofit is used.
 */

package com.vacationhome.app

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitClientFastAPI:
 * A Retrofit client specifically configured for FastAPI without "/api/" prefix.
 * Handles cookies manually to support session-based authentication.
 */
object RetrofitClientFastAPI {

    // Base URL of the FastAPI backend — no /api/ prefix
    private const val BASE_URL = "http://10.0.2.2:8000/"

    // Singleton Retrofit instance
    private var retrofit: Retrofit? = null

    // In-memory cookie storage
    private val cookieStore: HashMap<String, List<Cookie>> = HashMap()

    // CookieJar implementation to store and send cookies manually
    private val cookieJar = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.toString()] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.toString()] ?: ArrayList()
        }
    }

    /**
     * Returns a singleton instance of ApiService configured for FastAPI.
     */
    fun getInstance(context: Context): ApiService {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .cookieJar(cookieJar)
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
