package com.hdna.taskhouse.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hdna.taskhouse.R
import com.hdna.taskhouse.models.Rewards
import com.hdna.taskhouse.util.BaseRepository

class RewardsAdapter(
    var rewards: ArrayList<Rewards>, var ctx: Context
): RecyclerView.Adapter<RewardsAdapter.RewardsViewHolder>() {

    inner class RewardsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reward,parent,false)

        return RewardsViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("rewardsSize",rewards.size.toString())
        return rewards.size
    }

    override fun onBindViewHolder(holder: RewardsViewHolder, position: Int) {
        val holderView = holder.itemView
        var title = holderView.findViewById<TextView>(R.id.itemRewardTitle)
        var cost = holderView.findViewById<TextView>(R.id.itemRewardCost)
        var button  = holderView.findViewById<TextView>(R.id.itemClaimButton)

        cost.text = "Cost: " +  rewards[position].credits.toString()
        title.text = rewards[position].rewardTitle.toString()

        if(rewards[position].claimedBy != "Nobody")
        {
            button.text = "CLAIMED"
            cost.text = rewards[position].claimedBy
        }

        button.setOnClickListener {
            var arr = rewards
            var tmpReward = Rewards(arr[position].rewardTitle,arr[position].credits, "member1")
            arr[position] = tmpReward
            var repository = BaseRepository(ctx)
            repository.updateReward(arr)
            button.text = "CLAIMED"
            cost.text = rewards[position].claimedBy
        }

    }

}