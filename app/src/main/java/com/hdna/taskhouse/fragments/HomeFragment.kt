package com.hdna.taskhouse.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.R
import com.hdna.taskhouse.adapters.RewardsAdapter
import com.hdna.taskhouse.adapters.TasksAdapter
import com.hdna.taskhouse.models.PrimaryTask
import com.hdna.taskhouse.models.Rewards
import com.hdna.taskhouse.util.SharedPrefUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        var mainView = inflater.inflate(R.layout.fragment_home, container, false)



        var recyclerViewRewards = mainView.findViewById<RecyclerView>(R.id.taskRecyclerView)
        var tasks :  ArrayList<PrimaryTask> = arrayListOf<PrimaryTask>()
        var baseUrl = "https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/"
        var mDatabase = Firebase.database(baseUrl).reference
        var sharedPref = SharedPrefUtil(mainView.context)

        var familyCode = sharedPref.getString("familyCode")!!
        var username = sharedPref.getString("name")
        var currUid = Firebase.auth.currentUser!!.uid.toString()

        var homeUsernameTextView = mainView.findViewById<TextView>(R.id.homeUsernameTextView)
        homeUsernameTextView.text = username
        var homeUidTextView = mainView.findViewById<TextView>(R.id.homeUidTextView)
        homeUidTextView.text = currUid

        var homeRefreshButton = mainView.findViewById<Button>(R.id.homeRefreshButton)



        var adapter = TasksAdapter(tasks,mainView.context)

        mDatabase.child("families").child(familyCode).child("tasks").get().addOnSuccessListener { it->
            for (postSnapshot in it.children) {
                val map = postSnapshot.getValue<PrimaryTask>()
                Log.d("taskArray1",map.toString())
                tasks.add(map!!)
            }
            Log.d("taskArray1",tasks.toString())
            adapter.tasks = tasks
            adapter.notifyDataSetChanged()

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        recyclerViewRewards.setHasFixedSize(true);
        recyclerViewRewards.layoutManager = LinearLayoutManager(mainView.context)
        recyclerViewRewards.adapter = adapter

        homeRefreshButton.setOnClickListener {

            var tmpTasks :  ArrayList<PrimaryTask> = arrayListOf<PrimaryTask>()
            mDatabase.child("families").child(familyCode).child("tasks").get().addOnSuccessListener { it->
                for (postSnapshot in it.children) {
                    val map = postSnapshot.getValue<PrimaryTask>()
                    Log.d("taskArray1",map.toString())
                    tmpTasks.add(map!!)
                }
                Log.d("taskArray1",tmpTasks.toString())
                adapter.tasks = tmpTasks
                adapter.notifyDataSetChanged()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }


        return mainView
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}