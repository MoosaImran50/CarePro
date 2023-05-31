package com.example.carepro

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*

class DailyFireBaseDAO:DailyDAO {
    private var context: Context
    private lateinit var dbRef: DatabaseReference //get the reference of firebase data base


    constructor(ctx: Context) {
        context = ctx
    }

    override fun insertCalories(std: Daily) {

        dbRef = FirebaseDatabase.getInstance().getReference("Calories")
        val iD = dbRef.push().key!!
        val day =
            Daily(std.currentdate, std.currentday, std.currentmonth, std.foodname, std.calories, std.email)
        val status = dbRef.child(iD!!).setValue(day)



        status.addOnSuccessListener {

            Toast.makeText(context, "Record  Saved in Calories Table", Toast.LENGTH_SHORT).show()


        }.addOnFailureListener {

            Toast.makeText(context, "Record Not Saved in Calories Table", Toast.LENGTH_SHORT).show()
        }


    }
    override fun readCalories(email:String?, iD: Int?,month:String?, callback: (conversationList: MutableList<Daily>) -> Unit) {

        val dailylist = mutableListOf<Daily>()




        dbRef = FirebaseDatabase.getInstance().getReference("Calories")


        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                dailylist.clear()
                if(snapshot.exists()) {
                    for (i in snapshot.children) {
                        val currentdate = i.child("currentdate").value.toString().trim().toInt()
                        val currentmonth = i.child("currentmonth").value.toString().trim()
                        val currentEmail = i.child("email").value.toString().trim()

                        if (currentdate == iD && currentmonth==month && currentEmail==email) {
                            val con = i.getValue(Daily::class.java) //fetches a particular node from the database

                            dailylist.add(con!!) //add the node to the arraylist !! ensures that the object is not null

                        }

                    }
                }

                callback(dailylist)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"FireBase!! Failed to Read Calories Data", Toast.LENGTH_SHORT).show()

            }

        })

    }

    override fun UpdateCalories(email:String?, iD: Int?, callback:  (Int) -> Unit) {
        var sum: Double=0.0

        dbRef = FirebaseDatabase.getInstance().getReference("Calories")


        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {

                    for (i in snapshot.children) {

                        val currentdate = i.child("currentdate").value.toString().trim().toInt()
                        val currentEmail = i.child("email").value.toString().trim()

                        if (currentdate == iD && currentEmail == email) {


                            val value = i.child("calories").value.toString().toDouble()


                            if (value != null) {
                                sum += value

                            }


                        }

                    }
                }

                callback(sum.toInt())

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"FireBase!! Failed to Read Data for Porgress Bar", Toast.LENGTH_SHORT).show()

            }

        })

    }

}