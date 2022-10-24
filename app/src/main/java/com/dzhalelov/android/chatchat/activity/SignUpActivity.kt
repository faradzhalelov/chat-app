package com.dzhalelov.android.chatchat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.dzhalelov.android.chatchat.R
import com.dzhalelov.android.chatchat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var signUpButton: AppCompatButton
    private lateinit var signInButton: AppCompatButton

    private lateinit var emailEdt: EditText
    private lateinit var passwordEdt: EditText
    private lateinit var confirmPasswordEdt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        signUpButton = findViewById(R.id.signUpButton)
        signInButton = findViewById(R.id.signInButton)

        emailEdt = findViewById(R.id.edtEmail)
        passwordEdt = findViewById(R.id.edtPassword)
        confirmPasswordEdt = findViewById(R.id.edtConfirmPassword)

        signUpButton.setOnClickListener {
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()
            val confirmPassword = confirmPasswordEdt.text.toString()

            checkCredentials(email, password, confirmPassword)

            registerUser(email, password)
        }

        signInButton.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkCredentials(
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(
                applicationContext,
                "email cant be empty",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(
                applicationContext,
                "password cant be empty",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(
                applicationContext,
                "confirmPassword cant be empty",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (password != confirmPassword) {
            Toast.makeText(
                applicationContext,
                "password not match",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "successful")
                    val user = FirebaseAuth.getInstance().getCurrentUser()
                    if (user != null) {
                        auth.uid?.let { addUserToDatabase(email, user.uid) }
                        val intent = Intent(
                            this@SignUpActivity,
                            UsersActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signUpW:failure", task.exception)
                    Toast.makeText(baseContext, "Sign up failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(email: String, uid: String) {
        databaseReference = Firebase.database.reference
        databaseReference.child("users").child(uid).setValue(User(uid, "", email, ""))
    }

    companion object {
        private const val TAG = "SignUpActivity"
    }
}