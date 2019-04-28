package com.jinsoft77.firebaseloginkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

  private lateinit var logoutButton : Button
  private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    logoutButton = findViewById(R.id.button_logout)
    logoutButton.setOnClickListener {
      mAuth.signOut()
      var intent : Intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
    }
  }

}