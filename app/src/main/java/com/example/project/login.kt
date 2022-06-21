package com.example.project

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private val default_web_client_id="1054996243021-2rnadq730kpkh29p3qe9nemhuq9skkcr.apps.googleusercontent.com"

    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 1
    private lateinit var googleSignInClient: GoogleSignInClient
    private val database= Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth= FirebaseAuth.getInstance()
        val emailid=findViewById<EditText>(R.id.email_id)
        val passwd=findViewById<EditText>(R.id.password_login)


        signUp.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }

        signIn.setOnClickListener {
            val email=emailid.text.toString()
            val password= passwd.text.toString()
            if(email.isEmpty()){
                emailid.setError("Please enter email ID")
                emailid.requestFocus()
            }
            if(password.isEmpty()){
                passwd.setError("Please enter password")
                passwd.requestFocus()

            }
            else {
                //progress bar visible
                progress_signin.isVisible=true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            progress_signin.isVisible=false
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            progress_signin.isVisible=false;
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            //this toast is showing when we login with google
                            Toast.makeText(
                                baseContext, "Authentication failed.mail",
                                Toast.LENGTH_SHORT
                            ).show()
                           // updateUI(null)
                        }
                    }
            }
        }

        /*google.setOnClickListener {
            //progress bar visible
            progress_signin.isVisible=true
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(default_web_client_id)
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)
            signIn()
        }*/
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

    override fun onBackPressed() {
        super.onBackPressed()
        progress_signin.isVisible=false
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        Toast.makeText(this, "Getting started...", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,MainActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                progress_signin.isVisible=false;
                // Google Sign In failed, update UI appropriately
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    //at this point we will save user details in database
                    val userDetail=RegistrationDetails(user?.displayName.toString(),user?.email.toString(),"",user!!.uid)
                    //saving data of user in real time database
                    database.getReference("Users").child(user.uid).setValue(userDetail).addOnSuccessListener {

                    }
                    updateUI(user)
                } else {
                    progress_signin.isVisible=false;
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
}