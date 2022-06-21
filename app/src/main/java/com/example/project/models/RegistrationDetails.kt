package com.example.project.models

class RegistrationDetails() {
    lateinit var name:String
    lateinit var email:String
    lateinit var password:String
    lateinit var uid:String

    constructor(name:String,email:String,password:String,uid:String):this(){
        this.name=name
        this.email=email
        this.password=password
        this.uid=uid
    }
}