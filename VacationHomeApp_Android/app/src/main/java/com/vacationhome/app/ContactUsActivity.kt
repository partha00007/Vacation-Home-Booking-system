/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This activity provides the "Contact Us" screen in the Vacanza app.
 * It displays contact information such as email or phone number for user support or general inquiries.
 * The activity extends BaseActivity to inherit shared toolbar and navigation drawer setup.
 */

package com.vacationhome.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView

// ContactUsActivity:
// Activity responsible for showing contact details and enabling navigation back to Home.
class ContactUsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        // Set up the standard toolbar and navigation drawer (from BaseActivity)
        setupToolbar()

        // Logo click navigates to HomeActivity
        findViewById<ImageView>(R.id.logoImage)?.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
