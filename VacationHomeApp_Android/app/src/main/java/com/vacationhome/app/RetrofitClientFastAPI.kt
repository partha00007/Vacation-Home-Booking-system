/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This object initializes a Retrofit client configured to communicate
 * with a FastAPI backend. It includes custom cookie handling to manage session cookies
 * manually via a CookieJar implementation. The singleton pattern ensures that only one
 * Retrofit instance is created during the app's lifecycle.
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
 * Retrofit client for FastAPI backend (no "/api/" prefix, since our router is mounted at "/listings")
 */
object RetrofitClientFastAPI {

    // CHANGE THIS: remove "/api/" because FastAPI is serving at e.g. http://10.0.2.2:8000/listings
    private const val BASE_URL = "http://10.0.2.2:8000/"

    private var retrofit: Retrofit? = null

    // In-memory cookie store (if you need session‐based authentication)
    private val cookieStore: HashMap<String, List<Cookie>> = HashMap()

    private val cookieJar = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.toString()] = cookies
        }
        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.toString()] ?: ArrayList()
        }
    }

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
