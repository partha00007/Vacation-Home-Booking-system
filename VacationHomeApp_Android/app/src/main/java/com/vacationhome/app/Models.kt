/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This file defines the core data models used across the Vacanza app.
 * It includes:
 * - Review: representing user reviews on a listing.
 * - Listing: representing detailed information about a vacation home listing.
 *   These models are used for parsing JSON data from the backend using Gson.
 */
package com.vacationhome.app.models

import com.google.gson.annotations.SerializedName

/** A single user review on a listing. */
data class Review(
    @SerializedName("reviewer_name")
    val username: String,

    @SerializedName("comments")
    val comment: String,

    val rating: Int
)


/**
 * A single listing returned by FastAPI’s /listings endpoint.
 * Only includes the fields the API actually returns.
 */
data class Listing(
    @SerializedName("_id")
    val id: String,
    val title: String,
    val location: String,
    val price: Double,
    val description: String,
    val reviews: List<Review>,

    /**
     * Client‐only image URL (not returned by the API).
     * We’ll fill this in manually in the Activity before passing to the adapter.
     */
    val imageUrl: String? = null,
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
