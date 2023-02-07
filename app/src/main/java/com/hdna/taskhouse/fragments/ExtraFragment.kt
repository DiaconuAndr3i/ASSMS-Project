package com.hdna.taskhouse.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.R
import com.hdna.taskhouse.adapters.RewardsAdapter
import com.hdna.taskhouse.models.Rewards
import com.hdna.taskhouse.util.BaseRepository
import com.hdna.taskhouse.util.SharedPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExtraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExtraFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mainLayout: View

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
        // Inflate the layout for this fragment
        mainLayout  = inflater.inflate(R.layout.fragment_extra, container, false)

        var recyclerViewRewards = mainLayout.findViewById<RecyclerView>(R.id.rewardsRecyclerView)
        var rewards :  ArrayList<Rewards> = arrayListOf<Rewards>()
        var baseUrl = "https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/"
        var mDatabase = Firebase.database(baseUrl).reference
        var sharedPref = SharedPrefUtil(mainLayout.context)
        var familyCode = sharedPref.getString("familyCode")!!

        var adapter = RewardsAdapter(rewards,mainLayout.context)

        mDatabase.child("families").child(familyCode).child("rewards").get().addOnSuccessListener { it->
            for (postSnapshot in it.children) {
                val map = postSnapshot.getValue<Rewards>()
                Log.d("taskArray1",map.toString())
                rewards.add(map!!)
            }
            Log.d("taskArray1",rewards.toString())
            adapter.rewards = rewards
            adapter.notifyDataSetChanged()

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        recyclerViewRewards.setHasFixedSize(true);
        recyclerViewRewards.layoutManager = LinearLayoutManager(mainLayout.context)
        recyclerViewRewards.adapter = adapter

        return mainLayout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExtraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExtraFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}