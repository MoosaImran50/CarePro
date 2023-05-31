package com.example.carepro

import com.google.firebase.auth.FirebaseAuth

class BMI (private var height: Double, private var weight: Double, private var email: String, private var gender:String) {
    private var value: Double
    private var health: String = ""

    private fun findHealth()
    {
        if (value < 18.5){
            health = "Underweight"
        }
        else{
            if ((value >= 18.5) && (value <= 24.9)){
                health = "Healthy"
            }
            else{
                if ((value >= 25) && (value <= 29.9)){
                    health = "Overweight"
                }
                else{
                    health = "Obese"
                }
            }
        }
    }

    init {
        //height in meters
        val heightInMetres = (height / 100)

        value = weight / (heightInMetres * heightInMetres)

        findHealth()
    }

    fun getBMI(): Double
    {
        return value
    }

    fun getHealth(): String
    {
        return health
    }

    fun getEmail(): String
    {
        return email
    }

    fun getGender(): String
    {
        return gender
    }
}