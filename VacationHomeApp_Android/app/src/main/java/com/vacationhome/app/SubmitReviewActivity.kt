/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This activity allows a logged-in user to submit a review for a selected listing.
 * It collects user input for the review comment and a rating (1–5), validates the input, and
 * sends it to the backend using a Retrofit API call. The review is associated with the listing ID.
 */

package com.vacationhome.app

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.vacationhome.app.models.Review
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import androidx.drawerlayout.widget.DrawerLayout
import android.view.Gravity


/**
 * Activity for submitting a new review to a listing.
 */
class SubmitReviewActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        findViewById<ImageButton>(R.id.backButton)?.setOnClickListener {
            finish() // Returns to ListingDetailsActivity
        }

        findViewById<ImageView>(R.id.customDrawerIcon)?.setOnClickListener {
            val drawer = findViewById<DrawerLayout>(R.id.drawerLayout)
            drawer.openDrawer(Gravity.END)
        }

        findViewById<ImageView>(R.id.logoImage)?.setOnClickListener {
            finish() // Also returns to ListingDetailsActivity
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_review)

        // Bind view components
        val reviewEditText = findViewById<EditText>(R.id.reviewEditText)
        val ratingEditText = findViewById<EditText>(R.id.ratingEditText)
        val submitButton = findViewById<Button>(R.id.submitReviewButton)

        // Retrieve shared preferences and listing ID
        sharedPreferences = getSharedPreferences("VacanzaPrefs", MODE_PRIVATE)
        val listingId = intent.getStringExtra("listing_id") ?: return
        val username = sharedPreferences.getString("username", "Guest") ?: "Guest"

        // Handle review submission
        submitButton.setOnClickListener {
            val comment = reviewEditText.text.toString().trim()
            val rating = ratingEditText.text.toString().trim().toIntOrNull()

            // Validate input
            if (comment.isEmpty() || rating == null || rating !in 1..5) {
                Toast.makeText(this@SubmitReviewActivity, "Enter valid review and rating (1–5)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create review object with user input
            val review = Review(username = username, comment = comment, rating = rating)

            // Launch API call in coroutine
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val api = RetrofitClientFastAPI.getInstance(this@SubmitReviewActivity)
                    api.addReview(listingId, review)

                    runOnUiThread {
                        Toast.makeText(this@SubmitReviewActivity, "Review submitted!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: HttpException) {
                    runOnUiThread {
                        Toast.makeText(this@SubmitReviewActivity, "Error submitting review: ${e.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@SubmitReviewActivity, "Unexpected error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
