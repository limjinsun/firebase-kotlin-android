package com.jinsoft77.firebaseloginkotlin.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User ( var nickname: String? = "",  var email: String? ="" )