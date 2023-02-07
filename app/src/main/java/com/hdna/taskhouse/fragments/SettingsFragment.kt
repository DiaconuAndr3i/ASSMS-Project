package com.hdna.taskhouse.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.R
import com.hdna.taskhouse.activities.LoginActivity
import com.hdna.taskhouse.util.SharedPrefUtil


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var points: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var mainView= inflater.inflate(R.layout.fragment_settings, container, false)

        var name = mainView.findViewById<TextView>(R.id.settingsUsernameTextView)
        points = mainView.findViewById<TextView>(R.id.settingsPointsTextView)
        var familyCode = mainView.findViewById<TextView>(R.id.settingsFamilyCode)
        var logout = mainView.findViewById<Button>(R.id.settingsLogoutButton)
        var auth: FirebaseAuth = Firebase.auth




        var spStorage = SharedPrefUtil(mainView.context)
        name.text = spStorage.getString("name")
        familyCode.text = spStorage.getString("familyCode")

        setPoints(mainView, auth.currentUser!!.uid.toString())

        logout.setOnClickListener {
            auth.signOut()
            val newIntent = Intent(mainView.context, LoginActivity::class.java)
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(newIntent)
        }





        return mainView
    }

    private fun setPoints(mainView: View?, currentUser: String) {
        var baseUrl = "https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/"
        var mDatabase = Firebase.database(baseUrl).reference
        Log.d("curr",currentUser)
        mDatabase.child("users").child(currentUser).child("credits").get().addOnSuccessListener { it->
            points.text = it.value.toString() + " points"

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}