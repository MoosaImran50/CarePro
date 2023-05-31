package com.example.carepro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DonorDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donor_details)
        getSupportActionBar()?.setTitle("Donors Details")

        val donorName = intent.getStringExtra("DonorName")
        val donorBloodGroup = intent.getStringExtra("DonorBloodGroup")
        val donorLocation = intent.getStringExtra("DonorLocation")
        val donorAge = intent.getStringExtra("DonorAge")
        val donorGender = intent.getStringExtra("DonorGender")
        val donorContact = intent.getStringExtra("DonorContact")

        val name = findViewById<TextView>(R.id.nameValue)
        val bloodGroup = findViewById<TextView>(R.id.blood_groupValue)
        val location = findViewById<TextView>(R.id.locationValue)
        val gender = findViewById<TextView>(R.id.genderValue)
        val age = findViewById<TextView>(R.id.ageValue)
        val contact = findViewById<TextView>(R.id.contactValue)

        name.text = donorName
        bloodGroup.text = donorBloodGroup
        location.text = donorLocation
        gender.text = donorGender
        age.text = donorAge
        contact.text = donorContact

    }

}