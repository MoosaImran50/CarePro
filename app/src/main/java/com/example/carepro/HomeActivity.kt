package com.example.carepro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.carepro.bmiActivity
import com.example.carepro.databinding.CalorieCounterActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.Manifest
import android.content.pm.PackageManager


class HomeActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
    private lateinit var user_Details: IRegisterDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Location permission not granted
            // Request permission here
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }

        user_Details = RegisterDAO(this)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email_ = currentUser!!.email!!
        //val name_ = user_Details.readName(email_)
        val bmi_button = findViewById<ImageButton>(R.id.bmicalculator)
        val calorie_button = findViewById<ImageButton>(R.id.calorietracker)
        val donation_button = findViewById<ImageButton>(R.id.blood_donation)
        val hospital_button = findViewById<ImageButton>(R.id.nearesthospitals)
        val signout_button = findViewById<Button>(R.id.signout)
        val textView= findViewById<TextView>(R.id.textView)
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val displayName = email?.substringBefore('@')
        textView.text = "CarePro"


        bmi_button.setOnClickListener{
            val intent = Intent(this@HomeActivity, bmiActivity::class.java)
            startActivity(intent)
        }

        calorie_button.setOnClickListener{
            val intent = Intent(this@HomeActivity, CalorieCounter::class.java)
            startActivity(intent)
        }

        donation_button.setOnClickListener{
            val intent = Intent(this@HomeActivity, VolunteerOrViewDonorsList::class.java)
            startActivity(intent)
        }

        hospital_button.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
        }

        signout_button.setOnClickListener{
            // Sign out the user from Firebase Authentication
            FirebaseAuth.getInstance().signOut()

            // Clear the saved user ID and update the "isUserLoggedIn" flag in SharedPreferences
            val sharedPref = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("userId", null)
            editor.putBoolean("isUserLoggedIn", false)
            editor.apply()
            val intent = Intent(this@HomeActivity, LoginPage::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        // Get the flag from the Intent
        val hideProgressBar = intent.getBooleanExtra("hideProgressBar", false)

        // Hide the progress bar if the flag is set to true
        if (hideProgressBar) {
            progressBar.visibility = View.GONE
        }
    }
}