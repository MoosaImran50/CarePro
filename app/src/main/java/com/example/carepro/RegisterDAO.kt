package com.example.carepro

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RegisterDAO : IRegisterDAO {
    private var context: Context
    private lateinit var dbRef: DatabaseReference //get the reference of firebase data base


    constructor(ctx: Context) {
        context = ctx
    }

    override fun addName(user:User) {

        dbRef = FirebaseDatabase.getInstance().getReference("User")
        Log.d("Hello", "Name: ${user.name}, Email: ${user.email}")
        dbRef.child(dbRef.push().key!!).setValue(user)

    }
    override fun readName(email: String): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        lateinit var userName:String

        lateinit var userEmail:String
        if (currentUser != null) {
            userEmail = currentUser.email!!
        }
        else {}

        dbRef = FirebaseDatabase.getInstance().getReference("User")
        val query= dbRef.orderByChild("email").equalTo(userEmail)
        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val user:User = snapshot.getValue(User::class.java)!!
                userName = user.name!!
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"FireBase!! Failed to Read Calories Data", Toast.LENGTH_SHORT).show()

            }
        })
        return userName
    }

}