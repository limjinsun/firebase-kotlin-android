package com.jinsoft77.firebaseloginkotlin

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseApp

class RegistrationActivity : AppCompatActivity() {

  private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    FirebaseApp.initializeApp(this);
    setContentView(R.layout.activity_registration)

    var emailTV: EditText = findViewById(R.id.email)
    var passwordTV: EditText = findViewById(R.id.password)
    var progressBar: ProgressBar = findViewById(R.id.progressBar)
    var regBtn: Button = findViewById(R.id.register);

    regBtn.setOnClickListener { v ->
      registerNewUser(progressBar, emailTV, passwordTV, v)
    }
  }

  private fun registerNewUser(
    progressBar: ProgressBar,
    emailTV: EditText,
    passwordTV: EditText,
    v: View
  ): Unit {

    v.hideKeyboard()
    progressBar.visibility = View.VISIBLE

    if (emailTV.text.isEmpty()) {
      Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
      progressBar.visibility = View.GONE
      return
    } else if (passwordTV.text.isBlank()) {
      Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show();
      progressBar.visibility = View.GONE
      return
    } else if (passwordTV.text.length < 6) {
      passwordTV.setError("Password should be more than 6 words.")
      progressBar.visibility = View.GONE
      return
    }

    mAuth.createUserWithEmailAndPassword(emailTV.text.toString(), passwordTV.text.toString())
      .addOnCompleteListener { task ->
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

  fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
  }
}
