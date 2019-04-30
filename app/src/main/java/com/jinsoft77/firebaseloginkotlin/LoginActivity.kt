package com.jinsoft77.firebaseloginkotlin

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.jinsoft77.firebaseloginkotlin.data.User

class LoginActivity : AppCompatActivity() {

  private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
  private var tag: String = "** DEBUG **"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.loading_db_connection)
    var progressBar = findViewById<ProgressBar>(R.id.progressBar)

    // Check if user already logged in, If user logged in, bring it to the activity
    if (mAuth.currentUser != null) {
      Log.wtf(tag, "User is not null")
      checkItHasNickName(progressBar)
    } else {
      FirebaseApp.initializeApp(this);
      setContentView(R.layout.activity_login)

      var emailTV = findViewById<EditText>(R.id.email)
      var passwordTV = findViewById<EditText>(R.id.password)
      var progressBar = findViewById<ProgressBar>(R.id.progressBar)
      var loginBtn = findViewById<Button>(R.id.login);
      var registerTV: TextView = findViewById(R.id.register_textview)

      loginBtn.setOnClickListener { v ->
        loginUserAccount(progressBar, emailTV, passwordTV, v)
      }

      registerTV.setOnClickListener {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
      }
    }
  }

  private fun loginUserAccount(
    progressBar: ProgressBar,
    emailTV: EditText,
    passwordTV: EditText,
    v: View
  ): Unit {

    v.hideKeyboard()
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
          Toast.makeText(applicationContext, "Login!", Toast.LENGTH_LONG).show()
          checkItHasNickName(progressBar)

        } else {
          Log.wtf("--tag", task.exception.toString())
          Toast.makeText(applicationContext, "Login failed! Please try again later", Toast.LENGTH_LONG).show()
          progressBar.visibility = View.GONE
        }
      }
  }

  private fun checkItHasNickName(progressBar: ProgressBar) {
    var mUser: FirebaseUser? = mAuth.currentUser
    val userID = mUser?.uid.toString()
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    database.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(p0: DataSnapshot) {
        val user = p0.getValue(User::class.java)
        if (user?.nickname == null) {
          progressBar.visibility = View.GONE
          val intent: Intent = Intent(this@LoginActivity, NickNameActivity::class.java)
          startActivity(intent)
        } else {
          progressBar.visibility = View.GONE
          val intent: Intent = Intent(this@LoginActivity, GroupActivity::class.java)
          startActivity(intent)
        }
      }

      override fun onCancelled(p0: DatabaseError) {
        Log.wtf(tag, "onCancelled", p0.toException())
      }
    })
  }

  fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
  }
}