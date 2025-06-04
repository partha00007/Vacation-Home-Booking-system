/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This abstract base activity provides common functionality shared across multiple
 * screens in the Vacanza app. It includes logic for:
 * - Configuring the top toolbar
 * - Handling the navigation drawer (open/close actions)
 * - Managing menu item clicks (Home, About, Contact, Login/Logout)
 * All activities like Home, AboutUs, etc., extend this class to maintain consistent UI behavior.
 */

package com.vacationhome.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

/**
 * BaseActivity:
 * Open class that provides shared toolbar and navigation drawer logic.
 * Child activities inherit from this to maintain a consistent app shell.
 */
open class BaseActivity : AppCompatActivity() {

    // Shared layout components used by child activities
    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navView: NavigationView
    protected lateinit var drawerButton: ImageView
    protected lateinit var drawerClose: ImageView
    protected lateinit var toolbar: Toolbar

    /**
     * Call this in child activities to initialize the toolbar and navigation drawer behavior.
     */
    protected fun setupToolbar() {
        toolbar = findViewById(R.id.topAppBar)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        // Handle click on custom hamburger menu icon to open drawer
        val menuIcon = findViewById<ImageView>(R.id.customDrawerIcon)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        // Handle click on 'X' close icon inside drawer header
        val headerView = navView.getHeaderView(0)
        drawerClose = headerView.findViewById(R.id.closeDrawerButton)
        drawerClose.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        // Handle menu item selection in the drawer
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
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
                    val prefs = getSharedPreferences("VacanzaPrefs", MODE_PRIVATE)
                    if (prefs.getBoolean("isLoggedIn", false)) {
                        // Perform logout
                        prefs.edit {
                            putBoolean("isLoggedIn", false)
                        }
                        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        // Navigate to login screen
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }
    }
}
