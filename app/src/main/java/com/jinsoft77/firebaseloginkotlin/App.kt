package com.jinsoft77.firebaseloginkotlin

import android.annotation.SuppressLint
import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class App : Application {

  constructor() // with out this, Error.

  @SuppressLint("MissingSuperCall")
  override fun onCreate(){
    FirebaseDatabase.getInstance().setPersistenceEnabled(true)
  }

}