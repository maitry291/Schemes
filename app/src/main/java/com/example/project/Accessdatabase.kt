package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.project.models.SchemesTable
import kotlinx.android.synthetic.main.activity_accessdatabase.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Accessdatabase : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accessdatabase)

        val apiSet=ApiController.getClient().create(apiSet::class.java)


        insert.setOnClickListener {
            //if ()
            pb1.isVisible=true

            val row=SchemesTable(dept_ID.text.toString(),Department.text.toString(),Integer.parseInt(scheme_id.text.toString()),
            scheme_name.text.toString(),scheme_type.text.toString(),scheme_info.text.toString(),
            gender.text.toString(),Integer.parseInt(income.text.toString()),disabilities.text.toString(),caste.text.toString(),
            Integer.parseInt(lower_age.text.toString()),Integer.parseInt(upper_age.text.toString()))

            val call=apiSet.insertData(row)

            // on below line we are executing our method.
            call.enqueue(object : retrofit2.Callback<SchemesTable>{
                override fun onResponse(
                    call: Call<SchemesTable>,
                    response: Response<SchemesTable>
                ) {
                    pb1.isVisible=false
                    Toast.makeText(this@Accessdatabase, "Scheme added", Toast.LENGTH_SHORT).show()
                    dept_ID.setText("")
                    Department.setText("")
                    scheme_id.setText("")
                    scheme_name.setText("")
                    scheme_type.setText("")
                    scheme_info.setText("")
                    gender.setText("")
                    income.setText("")
                    disabilities.setText("")
                    caste.setText("")
                    lower_age.setText("")
                    upper_age.setText("")
                }

                override fun onFailure(call: Call<SchemesTable>, t: Throwable) {
                    pb1.isVisible=false
                    Log.d("insert",t.toString())
                }
            })

        }
    }
}