package com.hdna.taskhouse.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.R
import com.hdna.taskhouse.models.PrimaryUser
import com.hdna.taskhouse.util.BaseRepository
import com.hdna.taskhouse.util.SharedPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

class JoinFamilyActivity : AppCompatActivity() {

    lateinit var generateFamilyButton: Button
    lateinit var familyJoinCodeEditText: EditText
    lateinit var familyJoinButton: Button
    var familyCode = "none"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_family)

        initUi()

    }

    private fun initUi() {
        generateFamilyButton = findViewById(R.id.generateFamilyButton)
        generateFamilyButton.setOnClickListener {
            val i = Intent(this, CreateFamily::class.java)
            startActivity(i)
        }

        familyJoinCodeEditText = findViewById(R.id.familyJoinCodeEditText)
        familyJoinButton = findViewById(R.id.joinFamilyButton)

        familyJoinButton.setOnClickListener {
            beginJoin()
        }


    }

    private fun beginJoin() {
        familyCode = familyJoinCodeEditText.text.toString()

        var spStorage = SharedPrefUtil(this)
        spStorage.put("familyCode",familyCode)

        var auth: FirebaseAuth = Firebase.auth
        val database = Firebase.database("https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/").reference

        val user = auth.currentUser!!.uid.toString()

        var repository = BaseRepository(this)
        val scope = CoroutineScope(newSingleThreadContext("mainForce"))
        runBlocking {
            scope.launch { repository.addFamilyMember(user)}
        }

        val i = Intent(this, DashboardActivity::class.java)
        startActivity(i)



    }
}