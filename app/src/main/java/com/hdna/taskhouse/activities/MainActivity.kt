package com.hdna.taskhouse.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.R
import com.hdna.taskhouse.util.SharedPrefUtil

class MainActivity : AppCompatActivity() {

    lateinit var spStorage : SharedPrefUtil
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        spStorage = SharedPrefUtil(this)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        //val database = Firebase.database("https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/").reference

        if(currentUser==null)
        {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }else if(spStorage.getString("familyCode")==null) {
            val i = Intent(this, JoinFamilyActivity::class.java)
            startActivity(i)
        }else
        {
            val i = Intent(this, DashboardActivity::class.java)
            startActivity(i)
        }

//
//
//        if(spStorage.getString("userEmail") == null)
//        {
//
//
//        }else
//        {
//
//        }





    }
}