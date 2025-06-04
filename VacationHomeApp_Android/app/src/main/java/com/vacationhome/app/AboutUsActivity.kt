/*
 * Developed by Sana Pervaiz
 * Matriculation Number: 28933
 *
 * App Name: Vacanza
 *
 * Description:
 * This activity displays information about the Vacanza app, such as its purpose,
 * developer background, or company mission. It uses a shared toolbar (inherited
 * from BaseActivity) and includes navigation to the home screen.
 */

package com.vacationhome.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

// AboutUsActivity:
// This screen presents information about the app and team.
// It extends BaseActivity to reuse common toolbar behavior.
class AboutUsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        // Set up toolbar from BaseActivity (shared UI layout)
        setupToolbar()

        // Handle logo click to return to HomeActivity
        findViewById<ImageView>(R.id.logoImage)?.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
