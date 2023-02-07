package com.hdna.taskhouse.util

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.models.PrimaryTask
import com.hdna.taskhouse.models.Rewards
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.tasks.asDeferred

class BaseRepository {

    lateinit var database: DatabaseReference
    var baseUrl = "https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/"
    var familyCode = "error"
    lateinit var ctx: Context

    constructor(ctx: Context) {
        this.ctx = ctx
        initDatabase()
    }

    fun initDatabase()
    {
        database = Firebase.database(baseUrl).reference
        var sharedPref = SharedPrefUtil(ctx)
        familyCode = sharedPref.getString("familyCode")!!
    }



    suspend fun addTask(task:PrimaryTask)
    {
        var taskArray :ArrayList<PrimaryTask> = getTasks()
        taskArray.add(task)
        database.child("families").child(familyCode).child("tasks").setValue(taskArray)
    }

    suspend fun getTasks(): ArrayList<PrimaryTask> {
        var taskArray :ArrayList<PrimaryTask> = arrayListOf()
        var dataSnapshot =  database.child("families").child(familyCode).child("tasks").get()
        val deferredDataSnapshot: Deferred<DataSnapshot> = dataSnapshot.asDeferred()
        val taskData: Iterable<DataSnapshot> = deferredDataSnapshot.await().children
        for (postSnapshot in taskData) {
                    val map = postSnapshot.getValue<PrimaryTask>()
                    Log.d("taskArray1",map.toString())
                    taskArray.add(map!!)
        }

        Log.d("taskArray",taskArray.toString())
        return taskArray
    }

    suspend fun addReward(reward : Rewards)
    {
        var rewardArray :ArrayList<Rewards> = getRewards()
        rewardArray.add(reward)
        database.child("families").child(familyCode).child("rewards").setValue(rewardArray)
    }

    fun updateReward(rewards : ArrayList<Rewards>)
    {
        database.child("families").child(familyCode).child("rewards").setValue(rewards)
    }
    fun updateTask(task : ArrayList<PrimaryTask>)
    {
        database.child("families").child(familyCode).child("tasks").setValue(task)
    }

    suspend fun getRewards(): ArrayList<Rewards> {
        var rewardArray :ArrayList<Rewards> = arrayListOf()
        var dataSnapshot =  database.child("families").child(familyCode).child("rewards").get()
        val deferredDataSnapshot: Deferred<DataSnapshot> = dataSnapshot.asDeferred()
        val taskData: Iterable<DataSnapshot> = deferredDataSnapshot.await().children
        for (postSnapshot in taskData) {
            val map = postSnapshot.getValue<Rewards>()
            Log.d("taskArray1",map.toString())
            rewardArray.add(map!!)
        }

        Log.d("rewardArray",rewardArray.toString())
        return rewardArray
    }

    suspend fun addFamilyMember(uid : String)
    {
        var membersArray :ArrayList<String> = getFamilyMembers()
        membersArray.add(uid)
        database.child("families").child(familyCode).child("members").setValue(membersArray)
    }

    suspend fun getFamilyMembers(): ArrayList<String> {
        var familyArray :ArrayList<String> = arrayListOf()
        var dataSnapshot =  database.child("families").child(familyCode).child("members").get()
        val deferredDataSnapshot: Deferred<DataSnapshot> = dataSnapshot.asDeferred()
        val taskData: Iterable<DataSnapshot> = deferredDataSnapshot.await().children
        for (postSnapshot in taskData) {
            val map = postSnapshot.getValue<String>()
            Log.d("familyArray1",map.toString())
            familyArray.add(map!!)
        }

        Log.d("FamilydArray",familyArray.toString())
        return familyArray
    }

    suspend fun getFamilyNiceNames(): ArrayList<String> {
        var membersArray :ArrayList<String> = getFamilyMembers()
        Log.d("members1",membersArray.toString())

        var finalNiceNames = getUserNiceNameArray(membersArray)
        Log.d("members2",finalNiceNames.toString())

        return finalNiceNames
    }


    suspend fun getUserNiceName(uid: String): String {
        var dataSnapshot =  database.child("users").child(uid).child("username").get()
        val deferredDataSnapshot: Deferred<DataSnapshot> = dataSnapshot.asDeferred()
        val taskData: DataSnapshot = deferredDataSnapshot.await()

        return taskData.value.toString()
    }

    suspend fun getUserNiceNameArray(uids: ArrayList<String>): ArrayList<String> {
        var familyArray :ArrayList<String> = arrayListOf()
        for(member in uids)
        {
            familyArray.add(getUserNiceName(member.toString()))
        }
        return familyArray
    }

}