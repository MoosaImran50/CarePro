package com.example.carepro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import com.example.carepro.databinding.BmiActivityBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import kotlin.math.roundToInt

class bmiActivity : AppCompatActivity() {

    private lateinit var binding: BmiActivityBinding
    private lateinit var bmi: BMI
    private var sex: String = "Male"
    private val dbManager = bmiDbManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BmiActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.weightPicker.minValue = 30
        binding.weightPicker.maxValue = 150

        binding.heightPicker.minValue = 100
        binding.heightPicker.maxValue = 250

        val maleButton =findViewById<RadioButton>(R.id.male_radio_button)
        val femaleButton =findViewById<RadioButton>(R.id.female_radio_button)
        val calculateButton = findViewById<Button>(binding.bmiButton.id)


        maleButton.setOnClickListener {
            sex = "Male"
            femaleButton.setChecked(false)
        }
        femaleButton.setOnClickListener {
            sex = "Female"
            maleButton.setChecked(false)
        }

        calculateButton.setOnClickListener()
        {
            binding.results.text = ""

            bmi = calculate()

            binding.results.text = String.format("Your BMI is: %.2f", bmi.getBMI())

            val imageView = findViewById<ImageView>(R.id.bmi_meter)

            if (bmi.getHealth()=="Underweight") {
                imageView.setImageResource(R.drawable.under)
            }
            else if (bmi.getHealth()=="Healthy") {
                imageView.setImageResource(R.drawable.normal)
            }
            else if (bmi.getHealth()=="Overweight") {
                imageView.setImageResource(R.drawable.over)
            }
            else if (bmi.getHealth()=="Obese") {
                imageView.setImageResource(R.drawable.obese)
            }
        }

        val updateButton = findViewById<Button>(binding.updateButton.id)

        updateButton.setOnClickListener()
        {
            Toast.makeText(this, "BMI Updated!", Toast.LENGTH_SHORT).show()
            //we only update if a new BMI has been calculated
            if (this::bmi.isInitialized)
            {
                if(dbManager.insert(bmi) == true)
                {
                    Toast.makeText(this, "Inserted!", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this, "No BMI Calculated!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculate() :BMI
    {
        val height = binding.heightPicker.value.toDouble()
        val weight = binding.weightPicker.value.toDouble()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email ?: ""
        return BMI(height, weight, email, sex)
    }
}