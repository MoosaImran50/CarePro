package com.example.carepro

data class Daily(var currentdate:Int?=null,var currentday:String?=null, var currentmonth:String?=null,var foodname:String?=null,var calories:Long=0, var email:String?=null){


}

interface DailyDAO {
    fun insertCalories(std: Daily)
    fun readCalories(email:String?, iD:Int?, month:String?, callback: (dailyList: MutableList<Daily>) -> Unit)
    fun UpdateCalories(email:String?, iD:Int?, callback: (Sum: Int) -> Unit)
}
