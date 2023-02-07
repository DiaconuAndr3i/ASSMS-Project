package com.hdna.taskhouse.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Rewards(val rewardTitle: String? = null, val credits: Int? = 0,val claimedBy: String? = "Nobody") {
}
