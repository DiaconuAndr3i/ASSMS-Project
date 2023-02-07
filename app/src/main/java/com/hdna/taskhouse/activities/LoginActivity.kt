package com.hdna.taskhouse.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.R
import com.hdna.taskhouse.models.PrimaryUser
import com.hdna.taskhouse.util.SharedPrefUtil

class LoginActivity : AppCompatActivity() {

    lateinit var  emailEditText: EditText
    lateinit var  passwordEditText : EditText
    lateinit var  loginButton : Button
    lateinit var  goToRegisterTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUi()


    }

    private fun initUi() {
        emailEditText = findViewById<EditText>(R.id.loginEmailEditText)
        passwordEditText = findViewById<EditText>(R.id.loginPasswordEditText)
        loginButton = findViewById<Button>(R.id.loginLoginButton)
        goToRegisterTextView = findViewById<Button>(R.id.loginNoAccountRegisterTextView)

        loginButton.setOnClickListener {
            var email = emailEditText.text.toString()
            var password = passwordEditText.text.toString()
            startLogin(email,password)
        }

        goToRegisterTextView.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }


    }

    private fun startLogin(email: String, password: String) {
        var spStorage = SharedPrefUtil(this)
        var auth: FirebaseAuth = Firebase.auth
        val database = Firebase.database("https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/").reference

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser!!.uid.toString()
                    Toast.makeText(this,"Succes",Toast.LENGTH_SHORT).show()

                    database.child("users").child(user).get().addOnSuccessListener { it->
                         var tmp :PrimaryUser = it.getValue<PrimaryUser>()!!
                         spStorage.put("familyCode",tmp.familyCode.toString())
                         spStorage.put("name",tmp.username.toString())
                         var intent = Intent(this,DashboardActivity::class.java)
                         startActivity(intent)

                    }.addOnFailureListener{
                        Log.e("firebase", "Error getting data", it)
                    }


                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
}