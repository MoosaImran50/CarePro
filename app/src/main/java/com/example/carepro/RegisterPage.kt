package com.example.carepro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterPage : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user_Details: IRegisterDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)
        firebaseAuth = FirebaseAuth.getInstance()

        user_Details = RegisterDAO(this)
        val nameText = findViewById<EditText>(R.id.name)
        val emailText = findViewById<EditText>(R.id.email)
        val passwordText = findViewById<EditText>(R.id.password)
        val cpasswordText = findViewById<EditText>(R.id.confirmpassword)
        val LoginButton = findViewById<Button>(R.id.login_button)
        val signupButton = findViewById<Button>(R.id.signup_button)
        var name: String
        var email:String
        var password:String
        var cpass:String

        LoginButton.setOnClickListener{
            val intent = Intent(this@RegisterPage, LoginPage::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener {
            signupButton.isEnabled = false
            name = nameText.text.toString().trim()
            email = emailText.text.toString().trim()
            password = passwordText.text.toString().trim()
            cpass = cpasswordText.text.toString().trim()
            if (email == "") {
                Toast.makeText(
                    this@RegisterPage,
                    "Email field can not be left blank!",
                    Toast.LENGTH_SHORT
                ).show()
                signupButton.isEnabled = true
            }
            else if (name == "") {
                Toast.makeText(
                    this@RegisterPage,
                    "Name field can not be left blank!",
                    Toast.LENGTH_SHORT
                ).show()
                signupButton.isEnabled = true
            }
            else if (password == "") {
                Toast.makeText(
                    this@RegisterPage,
                    "Password field can not be left blank!",
                    Toast.LENGTH_SHORT
                ).show()
                signupButton.isEnabled = true
            }
            else if (cpass == "") {
                Toast.makeText(
                    this@RegisterPage,
                    "Confirm password field can not be left blank!",
                    Toast.LENGTH_SHORT
                ).show()
                signupButton.isEnabled = true
            }
            else
            {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            user_Details.addName(User(email, name))
                            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterPage, LoginPage::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
                            signupButton.isEnabled = true
                        }
                    }
            }
        }
    }
}