/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This file defines the core data models used across the Vacanza app.
 * It includes:
 * - Review: represents a user-submitted review for a listing.
 * - Listing: represents a vacation home listing with detailed attributes.
 * These models are used for JSON parsing with Gson from the FastAPI backend.
 */

package com.vacationhome.app.models

import com.google.gson.annotations.SerializedName

/**
 * Review:
 * Represents a single user review for a listing.
 * Contains the reviewer's name, comment text, and a rating value.
 */
data class Review(
    @SerializedName("reviewer_name")
    val username: String,

    @SerializedName("comments")
    val comment: String,

    val rating: Int
)

/**
 * Listing:
 * Represents a full vacation home listing returned by the backend.
 * Includes both API-returned data and optional UI-only fields (like imageUrl).
 */
data class Listing(
    @SerializedName("_id")
    val id: String,

    val title: String,
    val location: String,
    val price: Double,
    val description: String,
    val reviews: List<Review>,

    // UI-only field: assigned manually in frontend to show listing image
    val imageUrl: String? = null,

    // Optional details from MongoDB sample_airbnb dataset
    val summary: String? = null,
    val space: String? = null,
    val neighborhood_overview: String? = null,
    val notes: String? = null,
    val transit: String? = null,
    val access: String? = null,
    val interaction: String? = null,
    val house_rules: String? = null,
    val property_type: String? = null,
    val room_type: String? = null,
    val bed_type: String? = null,
    val minimum_nights: Int = 1,
    val maximum_nights: Int = 30,
    val accommodates: Int = 1,
)
