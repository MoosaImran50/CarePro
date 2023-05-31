package com.example.carepro

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.math.roundToInt


class CalorieCounter : AppCompatActivity() {
    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1
    }

    private val CHANNEL_ID = "channelID1"
    private val notification_id = 101
    private val TAG = "Calorie"
    private var currrentCalories: Int = 0
    private var totalCalories: Double = 1800.0
    private lateinit var etFoodName: EditText
    private lateinit var etCalories: EditText
    private lateinit var tvProgress: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var historybutton: Button
    private lateinit var savebutton: Button
    private lateinit var DDAO:  DailyDAO //MESSAGE domain layer object
//    private lateinit var obj:Daily



    class MyClass {
        companion object {
            var total2: Int = 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.calorie_counter_activity)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Notification permission not granted
            // Request permission here
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                CalorieCounter.REQUEST_NOTIFICATION_PERMISSION
            )
        }

        createNotification()

        var daily_list = mutableListOf<Daily>()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        val databaseReference = FirebaseDatabase.getInstance().reference
        val userId = uid

        progressBar = findViewById(R.id.progress_bar)


        if (userId != null) {
            databaseReference.child("BMIs").child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var bmi = dataSnapshot.child("bmi").getValue(Double::class.java)
                    var gender = dataSnapshot.child("gender").getValue(String::class.java)
                    if(bmi==null)
                    {
                        bmi = 20.0
                    }
                    if(gender==null)
                    {
                        gender = "Male"
                    }
                    Log.d(TAG, "User's BMI is: $bmi, Gender is: $gender, Calories: $totalCalories")
                    totalCalories = calculateCalories(bmi!!, gender!!)
                    val intCalories = totalCalories.roundToInt()
                    progressBar.max = intCalories // set max value of progress bar
                    Log.d(TAG, "Integer calories: $intCalories")
                // Do something with the retrieved gender value
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, "Error: $databaseError")
                }
            })
        }


        etFoodName = findViewById(R.id.edit_text_food_name)
        etCalories = findViewById(R.id.edit_text_calories)
        tvProgress = findViewById(R.id.text_view_progress)
        historybutton=findViewById(R.id.button_history)
        savebutton=findViewById(R.id.button_save)
        DDAO=DailyFireBaseDAO(this)

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) //Returns name of the day
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) //Return date
        val month = calendar.get(Calendar.MONTH) //Returns name of month

        //The max value will be set by user which will be recieved through intent in this activity


        val max_calories=progressBar.max
        val email_ = currentUser!!.email.toString().trim()!!

        DDAO.UpdateCalories(email_, dayOfMonth) { sum ->
            progressBar.progress = sum
            tvProgress.text = "$sum kcal of \n${progressBar.max}  kcal"

        }


        savebutton.setOnClickListener {
            val foodname=etFoodName.text.toString().trim()
            val count=etCalories.text.toString().trim()
            if (foodname.isNotEmpty() && count.isNotEmpty()) {


                val day:String=getDayOfWeek(dayOfWeek)
                val monthname:String=getMonthName(month)
                val consumed=count.toLong()
                var cal= consumed.toInt()

                currrentCalories = progressBar.progress + cal


                daily_list.add( Daily(dayOfMonth,day,monthname,foodname,consumed,email_))

                DDAO.insertCalories( Daily(dayOfMonth,day,monthname,foodname,consumed, email_))

                DDAO.UpdateCalories(email_, dayOfMonth) { sum ->
                    progressBar.progress = sum
                    tvProgress.text = "$sum kcal of \n${progressBar.max}  kcal"

                }
                etCalories.text.clear()
                etFoodName.text.clear()

            }
            else{
                Toast.makeText(this,"Fields cannot be left Empty!",Toast.LENGTH_SHORT).show()
            }

            if(currrentCalories!=0)
            {
                sendNotification()
            }

        }
        historybutton.setOnClickListener {


            val intent = Intent(this@CalorieCounter, CalorieRecord::class.java)
            intent.putExtra("currentdate", dayOfMonth)
            startActivity(intent)

        }


    }

    private fun getDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> throw IllegalArgumentException("Invalid day of week: $dayOfWeek")
        }
    }
    private fun getMonthName(month: Int): String {
        return when (month) {
            0 -> "january"
            1 -> "february"
            2 -> "march"
            3 -> "april"
            4 -> "may"
            5 -> "june"
            6 -> "july"
            7 -> "august"
            8 -> "september"
            9 -> "october"
            10 -> "november"
            11 -> "december"
            else -> throw IllegalArgumentException("Invalid month value: $month")
        }
    }

    private fun calculateCalories(bmi:Double,gender:String): Double
    {
        if(gender == "Male")
        {
            return (1600 + (11.3 * bmi))
        }
        else
        {
            return (1400 + (8.9 * bmi))
        }
    }

    private fun sendNotification() {
        // Create an explicit intent that launches your app's main activity
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Build the notification with the content intent
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.carepro_logo)
            .setContentTitle("Calories")
            .setContentText("Your today's total calories: $currrentCalories")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Show the notification using the builder object
        notificationManager.notify(notification_id, builder.build())
    }

    private fun createNotification(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name="Notification Name"
            val descriptionText="Calorie NOTIFICATION"
            val importance=NotificationManager.IMPORTANCE_DEFAULT
            val channel= NotificationChannel(CHANNEL_ID,name,importance).apply {
                description=descriptionText
            }
            val notificationManager:NotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
