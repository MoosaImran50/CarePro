package com.example.carepro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth;

class LoginPage : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login_page)
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if the user is already logged in based on the saved user ID
        val sharedPref = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)
        val isLoggedIn = sharedPref.getBoolean("isUserLoggedIn", false)

        if (userId != null && isLoggedIn) {
            // User is already logged in, proceed to the main activity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val emailText = findViewById<EditText>(R.id.email)
        val passwordText = findViewById<EditText>(R.id.password)
        val LoginButton = findViewById<Button>(R.id.login_button)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val createAccountButton = findViewById<Button>(R.id.create_account_button)
        var email:String
        var password:String

        resetButton.setOnClickListener{
            val intent = Intent(this@LoginPage, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        createAccountButton.setOnClickListener{
            val intent = Intent(this@LoginPage, RegisterPage::class.java)
            startActivity(intent)
        }
        LoginButton.setOnClickListener{
            LoginButton.isEnabled = false
            email = emailText.text.toString().trim()
            password = passwordText.text.toString().trim()
            if (email == "") {
                Toast.makeText(
                    this@LoginPage,
                    "Email field can not be left blank!",
                    Toast.LENGTH_SHORT
                ).show()
                LoginButton.isEnabled = true
            }
            else if (password == "") {
                Toast.makeText(
                    this@LoginPage,
                    "Password field can not be left blank!",
                    Toast.LENGTH_SHORT
                ).show()
                LoginButton.isEnabled = true
            }
            else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            val user = FirebaseAuth.getInstance().currentUser
                            val sharedPref = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putString("userId", user?.uid)
                            editor.putBoolean("isUserLoggedIn", true)
                            editor.apply()

                            val displayName = user?.displayName
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginPage, HomeActivity::class.java)
                            intent.putExtra("name", displayName)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                            LoginButton.isEnabled = true
                        }
                    }
            }
        }
    }
}