package com.example.project.models

class Registration() {
     var fname:String=""
     var lname:String=""
     var gender:String=""
     var bdate:String=""
     var email:String=""
     var bio:String=""
     var phone:String=""
    var scheme:String=""

    constructor(fname:String,lname:String,gender:String,bdate:String,email:String,bio:String,phone:String,scheme:String):this(){
        this.fname=fname
        this.lname=lname
        this.gender=gender
        this.bdate=bdate
        this.email=email
        this.bio=bio
        this.phone=phone
        this.scheme=scheme
    }
}