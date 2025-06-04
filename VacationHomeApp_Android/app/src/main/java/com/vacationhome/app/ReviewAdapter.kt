/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This RecyclerView adapter is used to display user reviews in a list format.
 * Each item includes the reviewer's username, comment, and star rating. It is used in
 * the ListingDetailsActivity to show up to 8 recent reviews for a selected vacation home.
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
 * RecyclerView adapter for displaying a list of reviews.
 */
class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    /**
     * ViewHolder representing a single review item.
     */
    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.reviewUsername)
        val commentText: TextView = itemView.findViewById(R.id.reviewComment)
        val ratingBar: RatingBar = itemView.findViewById(R.id.reviewRating)
    }

    /**
     * Inflates the layout for each review item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    /**
     * Binds the review data (username, comment, and rating) to the item view.
     */
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.usernameText.text = review.username
        holder.commentText.text = review.comment
        holder.ratingBar.rating = review.rating.toFloat()
        Log.d("REVIEW_ADAPTER", "Review: ${review.username} - ${review.comment}")

    }

    /**
     * Returns the total number of reviews.
     */
    override fun getItemCount(): Int = reviews.size
}
