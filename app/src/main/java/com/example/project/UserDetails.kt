package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.project.models.Registration
import com.example.project.models.RegistrationDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetails : AppCompatActivity() {

    private val db=Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        //when user has registered in any scheme

        db.getReference("Registration").child(FirebaseAuth.getInstance().currentUser!!.uid).
        addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val model = snapshot.getValue(Registration::class.java)
                    if (FirebaseAuth.getInstance().currentUser!!.uid == snapshot.key) {
                        if (model != null) {
                            name.text = model.fname + " " + model.lname
                            email.text = model.email
                            if (model.scheme != "") {
                                applied.text = "Yes"
                                sname.text = model.scheme
                                progressBar.isVisible = false
                                ll.isVisible = true
                            }
                        }
                    }
                }
                else{
                        //when user has not registered in any scheme
                        db.getReference("Users").child(FirebaseAuth.getInstance().
                        currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val model1=snapshot.getValue(RegistrationDetails::class.java)
                                if(FirebaseAuth.getInstance().currentUser!!.uid ==snapshot.key){
                                    if (model1 != null) {
                                        name.text=model1.name
                                        email.text=model1.email
                                        applied.text="No"
                                        progressBar.isVisible=false
                                        ll.isVisible=true
                                        ss.isVisible=false
                                        sname.isVisible=false
                                        button.isVisible=false
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //withdraw from scheme application
        button.setOnClickListener {
            db.getReference("Registration").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
            Toast.makeText(this, "Application withdrawn", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
        }

    }
}