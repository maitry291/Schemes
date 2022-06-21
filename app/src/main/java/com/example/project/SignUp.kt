package com.example.project

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.project.models.RegistrationDetails
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    private val default_web_client_id =
        "1054996243021-2rnadq730kpkh29p3qe9nemhuq9skkcr.apps.googleusercontent.com"

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val RC_SIGN_IN = 1
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = Firebase.database

        logIn.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
        }

        //manual sign up with email and password
        signup.setOnClickListener {
            val email = email_signup.text.toString()
            val password = password_signup.text.toString()
            val name = person.text.toString()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                if (email.isEmpty()) {
                    email_signup.error = "Please enter email ID"
                    email_signup.requestFocus()

                }
                if (password.isEmpty()) {
                    password_signup.error = "Please enter password"
                    password_signup.requestFocus()
                }
                if (name.isEmpty()) {
                    person.error = "Please enter name"
                    person.requestFocus()
                }
               // return@setOnClickListener
            }
            else {
                //progress bar visible
                progress_signup.isVisible = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //progress bar invisible
                            progress_signup.isVisible = false

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")

                            val user = auth.currentUser
                            //Toast.makeText(this, "user" + user!!.uid, Toast.LENGTH_SHORT).show()
                            //at this point we will save user details in database
                            //saving data of user in real time database
                            //not uploading..........

                            val userDetail = RegistrationDetails()
                            userDetail.email = email
                            userDetail.name = name
                            userDetail.password = password
                            userDetail.uid=user!!.uid

                            //saving data of user in real time database
                            database.getReference("Users").child(auth.uid.toString())
                                .setValue(userDetail)
                                .addOnSuccessListener {

                                }

                            updateUI(user)
                        } else {
                            progress_signup.isVisible = false
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, task.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
            }
        }

        /*google_signup.setOnClickListener {
            //progress bar visible
            progress_signup.isVisible = true
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(default_web_client_id)
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)
            signIn()

        }*/
    }

    override fun onBackPressed() {
        super.onBackPressed()
        progress_signup.isVisible = false
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //reload();
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        //startActivity(Intent(this, MainActivity::class.java))
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        Toast.makeText(this, "Getting started...", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }

    //google sign up
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                progress_signup.isVisible = false
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    //at this point we will save user details in database
                    val userDetail =
                        RegistrationDetails(user?.displayName.toString(), user?.email.toString(), "",user!!.uid)
                    //saving data of user in real time database
                    database.getReference("Users").child(user.uid).setValue(userDetail)
                        .addOnSuccessListener {

                        }
                    updateUI(user)
                } else {
                    progress_signup.isVisible = false
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //updateUI(null)
                }
            }
    }
}