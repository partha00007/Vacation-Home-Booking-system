/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This RecyclerView adapter is used to display user reviews in a vertical list format.
 * Each item includes the reviewer's username, comment, and star rating.
 * It is used within ListingDetailsActivity to display up to 8 recent reviews
 * for the selected vacation home.
 */

package com.vacationhome.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vacationhome.app.models.Review
import android.util.Log

/**
 * ReviewAdapter:
 * Adapter for rendering a list of reviews in a RecyclerView.
 */
class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    /**
     * ReviewViewHolder:
     * Holds references to each review item’s UI components.
     */
    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.reviewUsername)
        val commentText: TextView = itemView.findViewById(R.id.reviewComment)
        val ratingBar: RatingBar = itemView.findViewById(R.id.reviewRating)
    }

    /**
     * Inflates item_review layout for each row in the list.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    /**
     * Binds a Review object to the current ViewHolder.
     */
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.usernameText.text = review.username
        holder.commentText.text = review.comment
        holder.ratingBar.rating = review.rating.toFloat()

        // Debug log (optional)
        Log.d("REVIEW_ADAPTER", "Review: ${review.username} - ${review.comment}")
    }

    /**
     * Returns the number of review items in the list.
     */
    override fun getItemCount(): Int = reviews.size
}
