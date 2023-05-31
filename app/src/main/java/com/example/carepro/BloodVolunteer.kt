package com.example.carepro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carepro.Donor
import com.example.carepro.VolunteerOrViewDonorsList

class BloodVolunteer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blood_volunteer)
        getSupportActionBar()?.setTitle("Volunteer")

        val nameField = findViewById<EditText>(R.id.name)
        val bloodGroupField = findViewById<EditText>(R.id.bloodGroup)
        val locationField = findViewById<EditText>(R.id.location)
        val ageField = findViewById<EditText>(R.id.age)
        val contactField = findViewById<EditText>(R.id.phone)
        val submitButton = findViewById<Button>(R.id.buttonRegister)
        val radioMaleButton = findViewById<RadioButton>(R.id.radioMale)
        val radioFemaleButton = findViewById<RadioButton>(R.id.radioFemale)

        var name: String
        var bloodGroup: String
        var location: String
        var age: String
        var contact: String
        var gender = ""

        radioMaleButton.setOnClickListener(View.OnClickListener {
            radioFemaleButton.isChecked = false
            gender = "Male"
        })

        radioFemaleButton.setOnClickListener(View.OnClickListener {
            radioMaleButton.isChecked = false
            gender = "Female"
        })

        // onclick listener for adding contact button
        submitButton.setOnClickListener {
            name = nameField.text.toString().trim()
            bloodGroup = bloodGroupField.text.toString().trim()
            location = locationField.text.toString().trim()
            age = ageField.text.toString()
            contact = contactField.text.toString()

            if (name.isEmpty() || bloodGroup.isEmpty() || location.isEmpty() || age.isEmpty() || gender.isEmpty() || contact.isEmpty()) {
                Toast.makeText(
                    this@BloodVolunteer,
                    "Please Fill all the fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                Donor(contact, name, bloodGroup, location, age.toInt(), gender).addDonor()

                Toast.makeText(
                    this@BloodVolunteer,
                    "Volunteer added successfully!",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@BloodVolunteer, VolunteerOrViewDonorsList::class.java)
                startActivity(intent)

            }

        }

    }

}