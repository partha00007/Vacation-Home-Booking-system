/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This activity provides the "Contact Us" page for the Vacanza app.
 * It allows users to view contact information for support or inquiries.
 * It extends BaseActivity to inherit toolbar and navigation drawer behavior.
 */

package com.vacationhome.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView

// Activity class that displays the Contact Us screen to the user
class ContactUsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        // Set up the shared toolbar and navigation drawer
        setupToolbar()

        // Navigate to Home when logo is clicked
        findViewById<ImageView>(R.id.logoImage)?.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
