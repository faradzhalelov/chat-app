package com.dzhalelov.android.chatchat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.dzhalelov.android.chatchat.R
import com.dzhalelov.android.chatchat.adapter.UserAdapter
import com.dzhalelov.android.chatchat.databinding.ActivityUsersBinding
import com.dzhalelov.android.chatchat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UsersActivity : AppCompatActivity() {

    var binding: ActivityUsersBinding? = null
    var database: FirebaseDatabase? = null
    var auth: FirebaseAuth? = null
    var users: ArrayList<User>? = null
    var userAdapter: UserAdapter? = null
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        database = FirebaseDatabase.getInstance()
        users = ArrayList()
        userAdapter = UserAdapter(this@UsersActivity, users!!)

        val layoutManager = GridLayoutManager(this@UsersActivity, 2)
        binding!!.mRec.layoutManager = layoutManager

        val profile = intent.getStringExtra("profileImage")

        Glide.with(this@UsersActivity).load(profile)
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(binding!!.profileCircleImage)

        binding!!.ivExit.setOnClickListener {
            val intent = Intent(
                this@UsersActivity,
                SignUpActivity::class.java
            )
            auth!!.signOut()
            startActivity(intent)
            finish()
        }

        binding!!.ivSetupProfile.setOnClickListener {
            val intent = Intent(
                this@UsersActivity,
                SetupProfileActivity::class.java
            )
            startActivity(intent)
            finish()
        }

        database!!.reference.child("users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        binding!!.mRec.adapter = userAdapter
        database!!.reference.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    users!!.clear()
                    for (snap in snapshot.children) {
                        val user: User? = snap.getValue(User::class.java)
                        if (user!!.uid.equals(FirebaseAuth.getInstance().uid)) {
                            users!!.add(user)
                        }
                    }
                    userAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!).setValue("online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currentId!!).setValue("offline")
    }

}