package com.dzhalelov.android.chatchat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.dzhalelov.android.chatchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var logInButton: AppCompatButton
    private lateinit var signUpButton: AppCompatButton

    private lateinit var emailEdt: EditText
    private lateinit var passwordEdt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        signUpButton = findViewById(R.id.signUpButton)
        logInButton = findViewById(R.id.logInButton)
        emailEdt = findViewById(R.id.edtEmail)
        passwordEdt = findViewById(R.id.edtPassword)

        logInButton.setOnClickListener {
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()

            checkCredentials(email, password)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(
                this@SignInActivity,
                SignUpActivity::class.java
            )
            startActivity(intent)
            finish()
        }

    }

    private fun checkCredentials(email: String, password: String) {
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(
                applicationContext,
                "Check email or password",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            val intent = Intent(
                                this@SignInActivity,
                                UsersActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    companion object {
        private const val TAG = "SignUpActivity"
    }
}