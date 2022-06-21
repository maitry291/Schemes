package com.example.project

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.project.adapters.Adapter
import com.example.project.models.Registration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class Register : AppCompatActivity() {

    private val db = Firebase.database
    private val auth = FirebaseAuth.getInstance().currentUser
    lateinit var birthDate: EditText
    lateinit var birthMonth: EditText
    lateinit var birthYear: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val spinner = findViewById<Spinner>(R.id.spinner_gender)
        birthDate = findViewById(R.id.birthdate_date)
        birthMonth = findViewById(R.id.birthdate_month)
        birthYear = findViewById(R.id.birthdate_year)

        val list2 = arrayListOf("Male", "Female", "OTHER")
        val adapter2 = Adapter(list2, this)
        spinner.adapter = adapter2

        personal_info.setOnClickListener {
            val fname = edt_firstname.text.toString()
            val lname = edt_lastname.text.toString()
            val gender = list2[spinner.selectedItemPosition]
            val bdate ="${birthDate.text}-${birthMonth.text}-${birthYear.text}"
            val bio = edt_bio.text.toString()
            val email = edt_email.text.toString()
            val phone = edt_phone.text.toString()
            val scheme = intent.getStringExtra("scheme_name")

            if (fname == "" || lname == "" || gender == "" || bdate == "" || bio == "" || email == "" || phone == "") {
                Toast.makeText(this, "Please Fill all details", Toast.LENGTH_SHORT).show()
            } else {
                val user = Registration(fname, lname, gender, bdate, email, bio, phone, scheme!!)

                db.getReference("Registration").child(auth!!.uid).setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, UserDetails::class.java))
                    }
            }

        }

        // Fragment required to pop dialog
        // extends -- DialogFragment (ofCourse)
        //         -- DatePickerDialog.OnDateSetListener (to specifically open DatePicker)
        /*class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

            // current date
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // dialog pop up
            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

                // Create a new instance of DatePickerDialog and return it
                val datePickerDialog =
                    // Show current date if already not selected
                    if (birthDate.text.toString() == "00") DatePickerDialog(
                        requireContext(),
                        this,
                        year,
                        month,
                        day
                    )
                    // Show selected date
                    else DatePickerDialog(
                        requireContext(),
                        this,
                        Integer.parseInt(birthYear.text.toString()),
                        Integer.parseInt(birthMonth.text.toString()) - 1,
                        Integer.parseInt(birthMonth.text.toString())
                    )


                datePickerDialog.show() // show to avoid NPE

                // color of positive response text
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)?.setTextColor(
                    ContextCompat.getColor(
                        this@Register,
                        R.color.light
                    )
                )

                // color of negative response text
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)?.setTextColor(
                    ContextCompat.getColor(
                        this@Register,
                        R.color.hint_color
                    )
                )

                return datePickerDialog
            }

            // to decide what to do with selected date
            override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {

                // add 0 as prefix if date is single digit
                findViewById<TextView>(R.id.birthdate_date).text =
                    if (day / 10 > 0) day.toString() else "0$day"

                // add 0 if month is single digit
                // January is considered as 0th month so adjust accordingly
                findViewById<TextView>(R.id.birthdate_month).text =
                    if ((month + 1) / 10 > 0) day.toString() else "0" + (month + 1)

                findViewById<TextView>(R.id.birthdate_year).text = year.toString()
            }
        }

        findViewById<LinearLayout>(R.id.personal_info_birthdate).setOnClickListener {
            val newFragment = DatePickerFragment() // instance of dialog fragment
            newFragment.show(supportFragmentManager, "datePicker") // show fragment
        }*/
    }
}