/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This interface defines the Retrofit API endpoints used by the Vacanza Android app
 * to communicate with the backend server. It includes methods for user login, signup, fetching listings,
 * posting new listings and reviews, and retrieving listing details by ID. Both suspend and non-suspend
 * versions are provided for compatibility with different parts of the app.
 */

package com.vacationhome.app

import com.vacationhome.app.models.Listing
import com.vacationhome.app.models.Review
import com.vacationhome.app.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit interface containing API endpoints for backend communication.
 */
interface ApiService {

    /**
     * Login endpoint using POST method.
     * Accepts a map of login credentials (username and password).
     */
    @POST("login/")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): Response<LoginResponse>

    /**
     * Fetch all listings from the backend (suspend function).
     */
    @GET("listings/")
    suspend fun getListings(): Response<List<Listing>>

    /**
     * Add a new listing to the backend (non-suspend for optional usage).
     */
    @POST("listings/")
    fun addListing(@Body listing: Listing): Call<Void>

    /**
     * Post a review for a specific listing identified by ID.
     */
    @POST("listings/{id}/reviews/")
    suspend fun addReview(
        @Path("id") id: String,
        @Body review: Review
    ): Response<Void>

    /**
     * Fetch all listings using a regular Call object (non-suspend version).
     */
    @GET("listings/")
    fun getAllListings(): Call<List<Listing>>

    /**
     * Fetch a specific listing by its ID.
     */
    @GET("listings/{id}/")
    suspend fun getListingById(@Path("id") id: String): Listing

    /**
     * Register a new user account.
     */
    @POST("register/")
    suspend fun signup(@Body credentials: Map<String, String>): Response<Void>
}
