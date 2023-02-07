package com.hdna.taskhouse.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Family(val uuid: String? = null, val headUUID: String? = null,val familyList: ArrayList<String>? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.

}
