/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This interface defines the Retrofit API endpoints used by the Vacanza Android app
 * to communicate with the backend server. It includes methods for:
 * - User authentication (login, signup)
 * - Fetching all listings
 * - Fetching a listing by ID
 * - Adding a new listing
 * - Posting a review for a listing
 * It supports both suspend (coroutines) and non-suspend (callback) approaches
 * for flexibility in different UI components.
 */

package com.vacationhome.app

import com.vacationhome.app.models.Listing
import com.vacationhome.app.models.Review
import com.vacationhome.app.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * ApiService:
 * Retrofit interface that defines all HTTP operations the app performs against the backend.
 */
interface ApiService {

    /**
     * Login (POST /login/)
     * Sends user credentials and receives login response with user data or token.
     */
    @POST("login/")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): Response<LoginResponse>

    /**
     * Fetch all listings (GET /listings/)
     * Coroutine-based version.
     */
    @GET("listings/")
    suspend fun getListings(): Response<List<Listing>>

    /**
     * Add a new listing (POST /listings/)
     * Regular Call version for compatibility with non-coroutine components.
     */
    @POST("listings/")
    fun addListing(@Body listing: Listing): Call<Void>

    /**
     * Post a review for a specific listing (POST /listings/{id}/reviews/)
     */
    @POST("listings/{id}/reviews/")
    suspend fun addReview(
        @Path("id") id: String,
        @Body review: Review
    ): Response<Void>

    /**
     * Get all listings (GET /listings/)
     * Regular Call version.
     */
    @GET("listings/")
    fun getAllListings(): Call<List<Listing>>

    /**
     * Get a listing by ID (GET /listings/{id}/)
     * Retrieves detailed data for a specific listing.
     */
    @GET("listings/{id}/")
    suspend fun getListingById(@Path("id") id: String): Listing

    /**
     * Signup (POST /register/)
     * Registers a new user with provided credentials.
     */
    @POST("register/")
    suspend fun signup(@Body credentials: Map<String, String>): Response<Void>
}
