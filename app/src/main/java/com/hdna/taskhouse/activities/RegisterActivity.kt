package com.hdna.taskhouse.activities

import android.content.ContentValues.TAG
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
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.R
import com.hdna.taskhouse.models.PrimaryUser
import com.hdna.taskhouse.util.SharedPrefUtil

class RegisterActivity : AppCompatActivity() {


    lateinit var  emailEditText: EditText
    lateinit var  passwordEditText : EditText
    lateinit var nameEditText: EditText
    lateinit var  registerButton : Button
    lateinit var  goToLoginTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initUi()

    }

    private fun initUi() {
        emailEditText = findViewById(R.id.registerEmailEditText)
        passwordEditText  = findViewById(R.id.registerPasswordEditText)
        nameEditText = findViewById(R.id.registerNameEditText)
        registerButton = findViewById(R.id.registerSubmitButton)
        goToLoginTextView = findViewById(R.id.registerNoAccountRegisterTextView)

        goToLoginTextView.setOnClickListener {
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            var name = nameEditText.text.toString()
            var password = passwordEditText.text.toString()
            var email = emailEditText.text.toString()

            processRegister(name,email,password)

        }

    }

    private fun processRegister(name: String, email: String, password: String) {
        var auth: FirebaseAuth = Firebase.auth
        val database = Firebase.database("https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/").reference
        var spStorage = SharedPrefUtil(this)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(this,"Succes",Toast.LENGTH_SHORT).show()

                    val userPrimary = PrimaryUser(name, email,0,"none",0)
                    database.child("users").child(auth.currentUser!!.uid).setValue(userPrimary)

                    spStorage.put("name",name)


                    var intent = Intent(this,JoinFamilyActivity::class.java)
                    startActivity(intent)

                } else {

                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    Toast.makeText(this,"NiceIt does not work",Toast.LENGTH_SHORT).show()
                }
            }
    }
}