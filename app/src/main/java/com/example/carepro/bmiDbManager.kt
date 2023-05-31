package com.example.carepro

import android.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class bmiDbManager() {

    private var database = FirebaseDatabase.getInstance().getReference("BMIs")

    //only insert new if record of user ID doesn't exist before, otherwise update
    fun insert(bmi: BMI): Boolean
    {
        var success = false
        val email = bmi.getEmail()
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        val existingQuery: Query = database.orderByChild("email").equalTo(email)
        existingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // user already has a record, update it instead of inserting
                    for (item in snapshot.children) {
                        item.ref.setValue(bmi)
                    }
                    success = true
                } else {
                    // insert new record
                    if (uid != null) {
                        database.child(uid).setValue(bmi)
                    }
                    success = true
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return success
    }
}