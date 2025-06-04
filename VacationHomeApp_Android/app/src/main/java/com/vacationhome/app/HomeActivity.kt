/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This is the main screen (Home) of the Vacanza app where vacation home listings
 * are displayed in a grid format. Users can filter listings by price range and sort
 * them in ascending or descending order. The screen includes a top toolbar and navigation drawer,
 * and all listings are fetched from the backend. This activity inherits common UI behavior
 * from BaseActivity.
 */

package com.vacationhome.app

import android.view.View
import android.widget.PopupMenu
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.vacationhome.app.models.Listing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * HomeActivity:
 * Displays listings in a grid layout with sorting and filtering features.
 * Inherits navigation and toolbar behavior from BaseActivity.
 */
class HomeActivity : BaseActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var filterButton: Button
    private lateinit var sortButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var listingAdapter: ListingAdapter

    private var listings: List<Listing> = listOf()             // All listings from backend
    private var currentData: List<Listing> = listOf()          // Filtered/sorted listings for display
    private var currentFilter: Pair<Int, Int> = 0 to Int.MAX_VALUE
    private var sortAscending: Boolean? = null                 // Sorting preference: true, false, or null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("VacanzaPrefs", MODE_PRIVATE)
        setupToolbar()

        // Set login/logout text based on user session state
        val loginItem = navView.menu.findItem(R.id.nav_login_logout)
        loginItem.title = if (sharedPreferences.getBoolean("isLoggedIn", false)) "Logout" else "Login"

        // Navigation drawer item handling
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    drawerLayout.closeDrawer(GravityCompat.END)
                    true
                }
                R.id.nav_about -> {
                    startActivity(Intent(this, AboutUsActivity::class.java))
                    true
                }
                R.id.nav_contact -> {
                    startActivity(Intent(this, ContactUsActivity::class.java))
                    true
                }
                R.id.nav_login_logout -> {
                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
                        sharedPreferences.edit {
                            putBoolean("isLoggedIn", false)
                        }
                        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        recreate()
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }

        // Initialize UI components
        filterButton = findViewById(R.id.filterButton)
        sortButton = findViewById(R.id.sortButton)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        listingAdapter = ListingAdapter(currentData)
        recyclerView.adapter = listingAdapter

        // Set up filter popup menu
        filterButton.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.apply {
                add("Under \$150")
                add("Under \$200")
                add("Under \$500")
                add("Over \$500")
                add("Remove Filter")
            }
            popup.setOnMenuItemClickListener { item ->
                currentFilter = when (item.title) {
                    "Under \$150" -> 0 to 150
                    "Under \$200" -> 0 to 200
                    "Under \$500" -> 0 to 500
                    "Over \$500" -> 500 to Int.MAX_VALUE
                    "Remove Filter" -> 0 to Int.MAX_VALUE
                    else -> currentFilter
                }
                applyFilterAndSort()
                true
            }
            popup.show()
        }

        // Set up sort popup menu
        sortButton.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.apply {
                add("Price: Low to High")
                add("Price: High to Low")
            }
            popup.setOnMenuItemClickListener { item ->
                sortAscending = when (item.title) {
                    "Price: Low to High" -> true
                    "Price: High to Low" -> false
                    else -> null
                }
                applyFilterAndSort()
                true
            }
            popup.show()
        }

        // Load listings from backend
        loadListings()
    }

    /**
     * Loads listings from the backend using Retrofit (asynchronously).
     */
    private fun loadListings() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClientFastAPI.getInstance(this@HomeActivity)
                val response = api.getListings()

                if (response.isSuccessful && response.body() != null) {
                    listings = response.body()!!
                    runOnUiThread { applyFilterAndSort() }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@HomeActivity, "Failed to load listings", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@HomeActivity, "Error fetching listings: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Filters and sorts the list of listings based on user selection.
     * Injects image URLs and updates the adapter.
     */
    private fun applyFilterAndSort() {
        // Filter listings by price
        var result = listings.filter { it.price >= currentFilter.first && it.price <= currentFilter.second }

        // Sort based on current sort direction
        result = when (sortAscending) {
            true -> result.sortedBy { it.price }
            false -> result.sortedByDescending { it.price }
            null -> result
        }

        // Predefined image URLs (10 total for demo)
        val imageUrls = listOf(
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

        // Inject images round-robin and trim to 15 items max
        currentData = result.take(15).mapIndexed { index, listing ->
            listing.copy(imageUrl = imageUrls[index % imageUrls.size])
        }

        // Refresh UI with new data
        listingAdapter = ListingAdapter(currentData)
        recyclerView.adapter = listingAdapter
    }
}
