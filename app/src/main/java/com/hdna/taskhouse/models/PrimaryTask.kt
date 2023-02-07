package com.hdna.taskhouse.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PrimaryTask(val taskTitle: String? = null,val startDate: String? = null,val endDate: String? = null,
                       val asignedTo: String? = null,val credits: Int? = 0) {
}
