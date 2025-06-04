/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This activity allows a logged-in user to submit a review for a selected listing.
 * It collects a comment and a rating from the user, validates the input,
 * and sends the review to the backend API via Retrofit.
 * The submitted review is linked to a listing using its ID.
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
 * SubmitReviewActivity:
 * Allows users to post a review with comment and rating to a specific listing.
 */
class SubmitReviewActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_review)

        // Bind view components
        val reviewEditText = findViewById<EditText>(R.id.reviewEditText)
        val ratingEditText = findViewById<EditText>(R.id.ratingEditText)
        val submitButton = findViewById<Button>(R.id.submitReviewButton)

        // SharedPreferences to get username
        sharedPreferences = getSharedPreferences("VacanzaPrefs", MODE_PRIVATE)
        val listingId = intent.getStringExtra("listing_id") ?: return
        val username = sharedPreferences.getString("username", "Guest") ?: "Guest"

        // Back button returns to ListingDetailsActivity
        findViewById<ImageButton>(R.id.backButton)?.setOnClickListener {
            finish()
        }

        // Drawer icon opens end drawer (if present)
        findViewById<ImageView>(R.id.customDrawerIcon)?.setOnClickListener {
            val drawer = findViewById<DrawerLayout>(R.id.drawerLayout)
            drawer.openDrawer(Gravity.END)
        }

        // Clicking the logo also returns to ListingDetailsActivity
        findViewById<ImageView>(R.id.logoImage)?.setOnClickListener {
            finish()
        }

        // Handle submit button logic
        submitButton.setOnClickListener {
            val comment = reviewEditText.text.toString().trim()
            val rating = ratingEditText.text.toString().trim().toIntOrNull()

            // Input validation
            if (comment.isEmpty() || rating == null || rating !in 1..5) {
                Toast.makeText(this@SubmitReviewActivity, "Enter valid review and rating (1–5)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Construct review object
            val review = Review(username = username, comment = comment, rating = rating)

            // Send review to backend
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
