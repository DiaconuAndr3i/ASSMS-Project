package com.hdna.taskhouse.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hdna.taskhouse.R
import com.hdna.taskhouse.models.PrimaryTask
import com.hdna.taskhouse.models.PrimaryUser
import com.hdna.taskhouse.util.BaseRepository


class TasksAdapter(
    var tasks: ArrayList<PrimaryTask>, var ctx: Context
): RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    inner class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task,parent,false)

        return TasksViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("tasksSize",tasks.size.toString())
        return tasks.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val holderView = holder.itemView
        var title = holderView.findViewById<TextView>(R.id.itemTaskTitle)
        var cost = holderView.findViewById<TextView>(R.id.itemRewardPrize)

        var asignedTo = holderView.findViewById<TextView>(R.id.itemAsignedTo)

        var startDate =  holderView.findViewById<TextView>(R.id.itemStartDate)
        var endDate =  holderView.findViewById<TextView>(R.id.itemEndDate)

        var button  = holderView.findViewById<TextView>(R.id.itemClaimTaskButton)

        title.text = tasks[position].taskTitle.toString()
        cost.text = tasks[position].credits.toString() + "p"

        asignedTo.text = tasks[position].asignedTo

        startDate.text = tasks[position].startDate
        endDate.text = tasks[position].endDate


        if(tasks[position].credits == 0)
        {
            button.text = "Done"
            button.setBackgroundColor(ctx.getColor(R.color.base_dark_blue))
        }
        else
        {
            button.setOnClickListener {
                var creditsReward = tasks[position].credits
                var arr = tasks
                var tmpTask = PrimaryTask(arr[position].taskTitle,arr[position].startDate,arr[position].endDate,arr[position].asignedTo,0)

                arr[position] = tmpTask
                var repository = BaseRepository(ctx)
                repository.updateTask(arr)
                button.text = "Done"
                button.setBackgroundColor(ctx.getColor(R.color.base_dark_blue))

                val currUser = Firebase.auth.currentUser!!.uid.toString()
                var baseUrl = "https://hometask-daed7-default-rtdb.europe-west1.firebasedatabase.app/"
                var mDatabase = Firebase.database(baseUrl).reference

                mDatabase.child("users").child(currUser).get().addOnSuccessListener { it->
                    var tmpUser: PrimaryUser? = it.getValue<PrimaryUser>()
                    Log.d("succesUser",tmpUser.toString())

                    Log.d("succesUser0",creditsReward.toString())
                    var newCredits = creditsReward!! + tmpUser!!.credits!!
                    Log.d("succesUser001",newCredits.toString())
                    mDatabase.child("users").child(currUser)
                        .setValue(PrimaryUser(tmpUser!!.username,tmpUser!!.email,
                            newCredits ,tmpUser!!.familyCode,tmpUser!!.premium))
                    Log.d("succesUser1",tmpUser.toString())
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }



            }

        }



    }

}