/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description: This activity displays information about the Vacanza app, such as the purpose of the app,
 * developer info, or company background. It extends BaseActivity to inherit shared UI behavior
 * (such as toolbar setup). It uses the layout defined in activity_about_us.xml.
 */

package com.vacationhome.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

// Activity class that presents an "About Us" page in the application
class AboutUsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        // Set up the common toolbar defined in BaseActivity
        setupToolbar()

        // Navigate to Home when logo is clicked
        findViewById<ImageView>(R.id.logoImage)?.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
