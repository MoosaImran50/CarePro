package com.example.carepro

data class User(var email:String?=null,var name:String?=null){
}

interface IRegisterDAO {
    fun addName(user: User)
    fun readName(email:String):String

}