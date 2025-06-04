/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This adapter is used by the RecyclerView in HomeActivity to display vacation home
 * listings in a two-column grid. It binds each listing's image, price, and title.
 * Clicking on a listing opens the ListingDetailsActivity by passing its ID and image URL.
 */

package com.vacationhome.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vacationhome.app.models.Listing

/**
 * ListingAdapter:
 * RecyclerView adapter that renders Listing items inside a grid layout.
 * Used in HomeActivity to display listings interactively.
 */
class ListingAdapter(private val listings: List<Listing>) :
    RecyclerView.Adapter<ListingAdapter.ListingViewHolder>() {

    // Fallback set of image URLs used when no imageUrl is provided by the backend
    private val imageUrls = listOf(
        "https://www.luxurychicagoapartments.com/wp-content/uploads/2023/03/DSC7197-scaled.jpg",
        "https://wpcdn.us-midwest-1.vip.tn-cloud.net/www.rimonthly.com/content/uploads/2022/08/n/i/406-emblem-125-mu-d2-cam4-scaled.jpg",
        "https://havenly.com/blog/wp-content/uploads/2023/03/leviaustin_021120_designerhometour_04.jpg",
        "https://media.architecturaldigest.com/photos/63d17172ab0dfee1a5855856/16:9/w_2560%2Cc_limit/UWS_Living-Room_016.jpeg",
        "https://media.angi.com/s3fs-public/studio-apartment-ideas-sliding-doors.jpeg",
        "https://www.gwinnettforum.com/wp-content/uploads/2015/03/15.0320.Viewsinside.jpg",
        "https://homeadore.com/wp-content/uploads/2023/06/apartment-for-a-bachelor-where-modern-design-meets-functionality-00030.jpg",
        "https://d28pk2nlhhgcne.cloudfront.net/assets/app/uploads/sites/3/2022/11/modern-studio-apartment-1220x671.jpg",
        "https://d34mfkth6cubud.cloudfront.net/wp-content/uploads/2022/10/12071242/Ideas-to-Decorate_Setup-a-Studio-Apartment-_-Cover-12-10-22.jpg",
        "https://media.architecturaldigest.com/photos/63767b2a06a085bce7f12cfa/16:9/w_2560%2Cc_limit/20220922_Marin_AlexBass_025.jpg"
    )

    /**
     * ListingViewHolder:
     * Holds references to UI components inside each item view.
     */
    inner class ListingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listingImage: ImageView = itemView.findViewById(R.id.listingImage)
        val listingPrice: TextView = itemView.findViewById(R.id.listingPrice)
        val listingDesc: TextView = itemView.findViewById(R.id.listingDescription)
    }

    /**
     * Inflates item_listing.xml for each item in the grid.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_listing, parent, false)
        return ListingViewHolder(view)
    }

    /**
     * Binds data to each grid item:
     * - Displays title and price
     * - Loads image using Glide
     * - Sets click listener to open ListingDetailsActivity
     */
    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val listing = listings[position]
        val context = holder.itemView.context

        holder.listingPrice.text = "Price: \$${listing.price}"
        holder.listingDesc.text = listing.title

        // Use listing image URL or fallback
        val imageUrl = listing.imageUrl ?: imageUrls[position % imageUrls.size]

        // Load image with Glide
        Glide.with(context)
            .load(imageUrl)
            .into(holder.listingImage)

        // On click: navigate to details screen with listing ID and image
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ListingDetailsActivity::class.java).apply {
                putExtra("listing_id", listing.id)
                putExtra("image_url", imageUrl)
            }
            context.startActivity(intent)
        }
    }

    /**
     * Returns total number of items in the list.
     */
    override fun getItemCount(): Int = listings.size
}
