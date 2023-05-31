package com.example.carepro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class CalorieRecord : AppCompatActivity() {

    private lateinit var rv: RecyclerView //recycler view type object
    private lateinit var adapter: DailyAdapter //adapter type object
    private lateinit var DDAO:  DailyDAO //MESSAGE domain layer object
    private lateinit var month: EditText
    private lateinit var date: EditText
    private lateinit var getbutton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calorie_record_activity)
//        val todaydate=intent.getIntExtra("currentdate",0)

        DDAO=DailyFireBaseDAO(this)

        rv = findViewById(R.id.recycler)
        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rv.layoutManager = LinearLayoutManager(this)

        var calorie_list = mutableListOf<Daily>()

        adapter = DailyAdapter(calorie_list)
        rv.adapter = adapter

        month=findViewById(R.id.monthInput)
        date=findViewById(R.id.dateInput)
        getbutton=findViewById(R.id.getButton)

        var day:Int=0
        var Month:String=""




        getbutton.setOnClickListener{

            var day2=date.text.toString().trim()
            var Month2=month.text.toString().trim()
            val currentUser = FirebaseAuth.getInstance().currentUser
            val email_ = currentUser!!.email.toString().trim()!!





            if(day2.isNotEmpty() && Month2.isNotEmpty()){

                if(day2.toInt()>=1 && day2.toInt()<=31) {

                    DDAO.readCalories(email_, day2.toInt(), Month2.lowercase()) { newcalorieList ->
                        calorie_list.clear()
                        calorie_list.addAll(newcalorieList.take(1))

                        if (calorie_list.size >= 1) {
                            adapter.notifyDataSetChanged()
                            rv.scrollToPosition(calorie_list.size - 1)

                            day = day2.toInt()
                            Month = Month2.lowercase()
                        } else {

                            Toast.makeText(this, "No Data Found in DataBase", Toast.LENGTH_SHORT)
                                .show()

                        }

                    }
                }
                else{
                    Toast.makeText(this,"Please Enter a Valid Date", Toast.LENGTH_SHORT).show()

                }


            }
            else{
                Toast.makeText(this,"Fields cannot be left Empty!", Toast.LENGTH_SHORT).show()
            }

        }

            adapter.setOnItemClickListener(object : DailyAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {

                    val intent = Intent(this@CalorieRecord, CalorieDetails::class.java)
                    intent.putExtra("currentdate", day)
                    intent.putExtra("currentmonth", Month)
                    startActivity(intent)

                }

            })

    }
}