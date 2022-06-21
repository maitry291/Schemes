package com.example.project.models

class SchemesTable(){
    //var ans:ArrayList<SchemesTable> = ArrayList()
    var dept_ID:String=""
    var Department:String=""
    var scheme_id:Int=0
    var scheme_name:String=""
    var scheme_type:String=""
    var scheme_info:String=""
    var gender:String=""
    var income:Int=0
    var disabilities:String=""
    var caste:String=""
    var lower_age:Int=0
    var upper_age:Int=0


    //, lower_income:Int, higher_income:Int, disability:String,
    constructor( dept_id:String,Department:String, scheme_id:Int,scheme_name:String,scheme_type:String,scheme_info:String,
                 gender:String, income:Int,disabilities:String, caste:String,lower_age:Int, upper_age:Int)
    :this(){
        this.dept_ID=dept_id
        this.Department=Department
        this.scheme_id=scheme_id
        this.scheme_name=scheme_name
        this.scheme_type=scheme_type
        this.scheme_info=scheme_info
        this.gender=gender
        this.income=income
        this.disabilities=disabilities
        this.lower_age=lower_age
        this.upper_age=upper_age
        this.caste=caste
    }

}