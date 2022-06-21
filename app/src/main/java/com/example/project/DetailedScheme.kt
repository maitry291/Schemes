package com.example.project

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detailed_scheme.*
import kotlinx.android.synthetic.main.alertfeedback.*

class DetailedScheme : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_scheme)

        schname.text = intent.getStringExtra("sname")
        schdept.text = intent.getStringExtra("sdept")
        schtype.append(intent.getStringExtra("stype"))
        schcaste.text = intent.getStringExtra("caste")
        schgender.text = intent.getStringExtra("gender")
        schage.append(
            intent.getIntExtra("slage", 0).toString() + "-" + intent.getIntExtra(
                "suage",
                0
            ) + " years"
        )
        schincome.append("Less than or equal to " + intent.getIntExtra("income", 0))
        schdisability.text = intent.getStringExtra("disability")
        schinfo.text = intent.getStringExtra("sinfo")

        register.setOnClickListener {
            val i = Intent(this, Register::class.java)
            i.putExtra("scheme_name", intent.getStringExtra("sname"))
            startActivity(i)
        }

        feedback.setOnClickListener {
            //open pop up to write review and store that review in database

            val builder = AlertDialog.Builder(this)
            // Get the layout inflater
            val inflater = this.layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.alertfeedback, null))

            //builder.create()
            builder.show()
        }

    }

}