package com.hdna.taskhouse.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.R
import com.hdna.taskhouse.models.Family
import com.hdna.taskhouse.models.PrimaryUser
import com.hdna.taskhouse.util.SharedPrefUtil
import java.util.*
import kotlin.collections.ArrayList

class CreateFamily : AppCompatActivity() {

    lateinit var generatedCode: EditText
    lateinit var acceptButton: Button
    lateinit var shareButton: Button
    lateinit var shareCode: String
    lateinit var spStorage : SharedPrefUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_family)

        spStorage = SharedPrefUtil(this)

        generatedCode = findViewById(R.id.familyCodeGeneratorEditText)
        shareCode = UUID.randomUUID().toString().replace("-","").substring(0,8).toString()
        generatedCode.setText(shareCode)

        initUi()

    }

    private fun initUi() {
        acceptButton =findViewById(R.id.acceptTermsButton)
        shareButton = findViewById(R.id.shareCodeButton)

        acceptButton.setOnClickListener {
            spStorage.put("familyCode",shareCode)
            val database = Firebase.database("https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/").reference

            var auth: FirebaseAuth = Firebase.auth
            var family = Family(shareCode,auth.currentUser!!.uid)
            database.child("families").child(shareCode).setValue(family)
            database.child("users").child(auth.currentUser!!.uid).child("familyCode").setValue(shareCode)

            val i = Intent(this, DashboardActivity::class.java)
            startActivity(i)
        }

        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT,
                "Join FamilyTask with the following code : $shareCode"
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share Via"))
        }

    }
}