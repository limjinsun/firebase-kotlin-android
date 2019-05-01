package com.jinsoft77.firebaseloginkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.jinsoft77.firebaseloginkotlin.data.User

class NickNameActivity : AppCompatActivity() {

  private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
  private var mUser: FirebaseUser? = mAuth.currentUser
  private val tag: String = "** DEBUG **"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_nickname)

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    var logoutButton: Button = findViewById(R.id.button_logout)
    var gotoGroupButton = findViewById<Button>(R.id.button_goto_group)
    var textView: TextView = findViewById(R.id.textView)
    var nickText: TextView = findViewById(R.id.nickname)
    var userID : String = mUser?.uid.toString()
    var userEmail : String = mUser?.email.toString()
    textView.text = mUser?.email

    database.keepSynced(true)

    gotoGroupButton.setOnClickListener {
      setNickNameAndGoToGroupActivity(nickText.text.toString(), database, userID, userEmail)
    }

    logoutButton.setOnClickListener {
      makeUserLogOutAndGotoLoginActivity()
    }
  }

  private fun setNickNameAndGoToGroupActivity(
    nickText: String,
    database: DatabaseReference,
    userID: String,
    userEmail: String
  ): Unit {

    if (nickText.isBlank()) {
      Toast.makeText(applicationContext, "Please enter NickNanme...", Toast.LENGTH_LONG).show()
      return
    }
    addUserNickNameToDB (userID, nickText, userEmail, database)
    var intent: Intent = Intent(this@NickNameActivity, GroupActivity::class.java)
    startActivity(intent)
  }

  private fun addUserNickNameToDB(
    userId: String,
    nickname: String,
    email: String?,
    database: DatabaseReference
  ): Unit {

    val user = User(nickname, email)
    database.child("users").child(userId).setValue(user).addOnCompleteListener { task ->
      if (task.isSuccessful) {
        Log.wtf(tag, "database save - success")
      } else {
        Log.wtf(tag, "database save - fail")
        Toast.makeText(applicationContext, "Database connection failed. Please Try again later..", Toast.LENGTH_LONG)
          .show()
      }
    }
  }

  private fun makeUserLogOutAndGotoLoginActivity(): Unit {
    mAuth.signOut()
    var intent: Intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
  }


}