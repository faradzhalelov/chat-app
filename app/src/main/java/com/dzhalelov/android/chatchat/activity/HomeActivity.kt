package com.dzhalelov.android.chatchat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dzhalelov.android.chatchat.R
import com.dzhalelov.android.chatchat.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    var binding: ActivityHomeBinding? = null
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        auth = FirebaseAuth.getInstance()

        binding!!.bacKButton.setOnClickListener {
            val intent = Intent(
                this@HomeActivity,
                SignUpActivity::class.java
            )
            auth!!.signOut()
            startActivity(intent)
            finish()
        }

        binding!!.userListButton.setOnClickListener {
            val intent = Intent(
                this@HomeActivity,
                UsersActivity::class.java
            )
            startActivity(intent)
            finish()
        }

        binding!!.setupProfileButton.setOnClickListener {
            val intent = Intent(
                this@HomeActivity,
                SetupProfileActivity::class.java
            )
            startActivity(intent)
            finish()
        }

    }
}