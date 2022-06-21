package com.example.project

import com.example.project.models.SchemesTable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface apiSet {

    @GET("select.php")
    fun getData():Call<List<SchemesTable>>  //will return the list which contains all rows of table as obj

    @POST("index.php")
    fun insertData(@Body dataModal:SchemesTable): Call<SchemesTable>

}