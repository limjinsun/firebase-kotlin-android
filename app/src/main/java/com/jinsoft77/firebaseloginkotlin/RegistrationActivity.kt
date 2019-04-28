package com.jinsoft77.firebaseloginkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseApp

class RegistrationActivity : AppCompatActivity() {

  private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
  lateinit var emailTV : EditText
  lateinit var passwordTV : EditText
  lateinit var progressBar : ProgressBar
  lateinit var regBtn : Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    FirebaseApp.initializeApp(this);
    setContentView(R.layout.activity_registration)

    initializeUI()
    regBtn.setOnClickListener {
      if(passwordTV.text.length < 6) {
        passwordTV.setError("Password should be more than 6 words.")
      } else {
        registerNewUser()
      }
    }
  }

  private fun initializeUI():Unit {
    emailTV = findViewById(R.id.email)
    passwordTV = findViewById(R.id.password)
    progressBar = findViewById(R.id.progressBar)
    regBtn = findViewById(R.id.register);
  }

  private fun registerNewUser(): Unit {
    progressBar.visibility = View.VISIBLE

    if (emailTV.text.isEmpty()) {
      Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
      return
    } else if (passwordTV.text.isBlank()){
      Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show();
      return
    }

    mAuth.createUserWithEmailAndPassword(emailTV.text.toString(), passwordTV.text.toString())
      .addOnCompleteListener{ task ->
        if (task.isSuccessful) {
          Log.i("--success", task.toString())
          Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG).show()
          progressBar.visibility = View.GONE

          val intent = Intent(this, LoginActivity::class.java)
          startActivity(intent)
        } else {
          Toast.makeText(applicationContext, "Registration failed! Please try again later", Toast.LENGTH_LONG).show()
          progressBar.visibility = View.GONE
        }
      }
  }
}
