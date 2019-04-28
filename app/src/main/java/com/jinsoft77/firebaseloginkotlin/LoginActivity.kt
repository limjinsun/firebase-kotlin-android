package com.jinsoft77.firebaseloginkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

  private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

  lateinit var emailTV: EditText
  lateinit var passwordTV: EditText
  lateinit var progressBar: ProgressBar
  lateinit var loginBtn: Button
  lateinit var registerTV: TextView
  private var tag : String = "****************** DEBUGGING ******************"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    FirebaseApp.initializeApp(this);
    setContentView(R.layout.activity_login)

    initialiseUI()

    // Check if user already logged in, If user logged in, bring it to main activity
    if(mAuth.currentUser != null){
      Log.wtf(tag, "User is not null")
      var intent : Intent = Intent(this, MainActivity::class.java)
      startActivity(intent)
    }

    loginBtn.setOnClickListener {
      loginUserAccount()
    }

    registerTV.setOnClickListener { v ->
      val intent = Intent(this, RegistrationActivity::class.java)
      startActivity(intent)
    }
  }


  private fun initialiseUI(): Unit {
    emailTV = findViewById(R.id.email)
    passwordTV = findViewById(R.id.password)
    progressBar = findViewById(R.id.progressBar)
    loginBtn = findViewById(R.id.login);
    registerTV = findViewById(R.id.register_textview)
  }


  private fun loginUserAccount(): Unit {
    progressBar.visibility = View.VISIBLE

    if (emailTV.text.isEmpty()) {
      Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
      return
    } else if (passwordTV.text.isBlank()) {
      Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show();
      return
    }

    mAuth.signInWithEmailAndPassword(emailTV.text.toString(), passwordTV.text.toString())
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG).show()
          progressBar.visibility = View.GONE
          val intent = Intent(this, MainActivity::class.java)
          startActivity(intent)
        } else {
          Log.wtf("--tag",task.exception.toString())
          Toast.makeText(applicationContext, "Login failed! Please try again later", Toast.LENGTH_LONG).show()
          progressBar.visibility = View.GONE
        }
      }
  }

}