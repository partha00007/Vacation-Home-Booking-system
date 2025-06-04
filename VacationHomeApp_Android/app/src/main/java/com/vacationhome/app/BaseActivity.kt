/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This abstract base activity provides common functionality shared across multiple
 * screens in the Vacanza app. It includes logic for handling the top toolbar, navigation drawer,
 * and menu item selection (e.g., home, about, contact, login/logout). All other activities extend this
 * class to inherit the consistent UI behavior defined here.
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
 * BaseActivity provides shared toolbar and navigation drawer functionality.
 * All main activities extend this to maintain consistent layout and navigation behavior.
 */
open class BaseActivity : AppCompatActivity() {

    // Drawer and toolbar UI components shared across activities
    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navView: NavigationView
    protected lateinit var drawerButton: ImageView
    protected lateinit var drawerClose: ImageView
    protected lateinit var toolbar: Toolbar

    /**
     * Call this method in child activities to configure the top app bar and navigation drawer.
     */
    protected fun setupToolbar() {
        toolbar = findViewById(R.id.topAppBar)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        // Open drawer when hamburger icon is clicked
        val menuIcon = findViewById<ImageView>(R.id.customDrawerIcon)
        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        // Close drawer when 'X' icon in header is clicked
        val headerView = navView.getHeaderView(0)
        drawerClose = headerView.findViewById(R.id.closeDrawerButton)
        drawerClose.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        // Handle navigation item selections
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
                        // Logout logic
                        prefs.edit {
                            putBoolean("isLoggedIn", false)
                        }
                        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        // Redirect to login
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }
    }
}
