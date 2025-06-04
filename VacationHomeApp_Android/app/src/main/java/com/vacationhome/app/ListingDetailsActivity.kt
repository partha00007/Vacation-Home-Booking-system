/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This activity shows the full details of a selected vacation home listing.
 * It displays the image, title, price, description, and up to 8 reviews.
 * The screen includes a Book button, a back button, and a conditional Add Review button
 * (only available if the user is logged in). Listing data is retrieved from the backend using Retrofit.
 */

package com.vacationhome.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vacationhome.app.models.Listing
import com.vacationhome.app.models.Review
import kotlinx.coroutines.launch

/**
 * ListingDetailsActivity:
 * Displays the full view of a vacation home listing.
 * Shows an image, metadata, and up to 8 user reviews.
 */
class ListingDetailsActivity : AppCompatActivity() {

    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewList = mutableListOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_details)

        // Get data passed from previous activity
        val listingId = intent.getStringExtra("listing_id")
        val passedImageUrl = intent.getStringExtra("image_url")

        if (listingId == null) {
            Toast.makeText(this, "Listing ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Setup review RecyclerView
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(reviewList)
        reviewRecyclerView.adapter = reviewAdapter

        // Back button returns to previous screen
        findViewById<ImageButton>(R.id.backButton)?.setOnClickListener {
            finish()
        }

        // Add Review button (checks if user is logged in first)
        findViewById<ImageButton>(R.id.addReviewButton)?.setOnClickListener {
            val prefs = getSharedPreferences("VacanzaPrefs", MODE_PRIVATE)
            val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

            if (!isLoggedIn) {
                Toast.makeText(this, "You must be logged in to write a review.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SubmitReviewActivity::class.java)
                intent.putExtra("listing_id", listingId)
                startActivity(intent)
            }
        }

        // Book button shows confirmation toast
        findViewById<Button>(R.id.bookButton)?.setOnClickListener {
            Toast.makeText(this, "Booking Successful!", Toast.LENGTH_SHORT).show()
        }

        // Load listing details and reviews from backend
        lifecycleScope.launch {
            try {
                val listing = RetrofitClient.getInstance(this@ListingDetailsActivity)
                    .getListingById(listingId)

                // Load image using passed URL or fallback to backend-provided image
                Glide.with(this@ListingDetailsActivity)
                    .load(passedImageUrl ?: listing.imageUrl)
                    .into(findViewById(R.id.detailImage))

                populateListingDetails(listing)
            } catch (e: Exception) {
                Toast.makeText(
                    this@ListingDetailsActivity,
                    "Error loading listing: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Populates the UI fields with listing data.
     * Displays up to 8 reviews in the RecyclerView.
     */
    private fun populateListingDetails(listing: Listing) {
        findViewById<TextView>(R.id.detailTitle).text = listing.title
        findViewById<TextView>(R.id.detailPrice).text = "$${listing.price}"
        findViewById<TextView>(R.id.detailDescription).text = "Listing ID: ${listing.id}"
        findViewById<TextView>(R.id.detailSummary).text = listing.description ?: "No summary"

        reviewList.clear()
        reviewList.addAll(listing.reviews.take(8))
        reviewAdapter.notifyDataSetChanged()
    }
}
