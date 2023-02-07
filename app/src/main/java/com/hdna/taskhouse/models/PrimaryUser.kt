package com.hdna.taskhouse.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PrimaryUser(val username: String? = null, val email: String? = null,val credits: Int? = 0,val familyCode: String? = null,val premium: Int? = 0) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
    override fun toString(): String {
        return "PrimaryUser(username=$username, email=$email, credits=$credits, familyCode=$familyCode, premium=$premium)"
    }
}
