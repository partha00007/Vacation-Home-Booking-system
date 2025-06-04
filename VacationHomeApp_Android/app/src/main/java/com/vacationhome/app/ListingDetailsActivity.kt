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

class ListingDetailsActivity : AppCompatActivity() {

    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private val reviewList = mutableListOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_details)

        val listingId = intent.getStringExtra("listing_id")
        val passedImageUrl = intent.getStringExtra("image_url")

        if (listingId == null) {
            Toast.makeText(this, "Listing ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        reviewRecyclerView = findViewById(R.id.reviewRecyclerView)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(reviewList)
        reviewRecyclerView.adapter = reviewAdapter

        findViewById<ImageButton>(R.id.backButton)?.setOnClickListener {
            finish()
        }

        // ✅ Add Review Button with Login Check
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

        // ✅ Book Button shows confirmation toast
        findViewById<Button>(R.id.bookButton)?.setOnClickListener {
            Toast.makeText(this, "Booking Successful!", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            try {
                val listing = RetrofitClient.getInstance(this@ListingDetailsActivity)
                    .getListingById(listingId)

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
