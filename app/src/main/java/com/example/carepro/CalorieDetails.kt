package com.example.carepro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class CalorieDetails : AppCompatActivity() {

    private lateinit var rv: RecyclerView //recycler view type object
    private lateinit var adapter: DetailsAdapter //adapter type object
    private lateinit var DDAO: DailyDAO //MESSAGE domain layer object

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calorie_details_activity)

        val todaydate = intent.getIntExtra("currentdate", 0)
        val recentmonth=intent.getStringExtra("currentmonth")


        DDAO = DailyFireBaseDAO(this)

        rv = findViewById(R.id.recycler2)
        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rv.layoutManager = LinearLayoutManager(this)

        var calorie_list = mutableListOf<Daily>()

        adapter = DetailsAdapter(calorie_list)
        rv.adapter = adapter

        val currentUser = FirebaseAuth.getInstance().currentUser
        val email_ = currentUser!!.email.toString().trim()!!

        DDAO.readCalories(email_, todaydate,recentmonth) { newcalorieList ->
            calorie_list.clear()
            calorie_list.addAll(newcalorieList)
            adapter.notifyDataSetChanged()
            rv.scrollToPosition(calorie_list.size-1)

        }


    }
}