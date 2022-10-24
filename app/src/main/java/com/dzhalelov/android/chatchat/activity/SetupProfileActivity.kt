package com.dzhalelov.android.chatchat.activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dzhalelov.android.chatchat.R
import com.dzhalelov.android.chatchat.databinding.ActivitySetupProfileBinding
import com.dzhalelov.android.chatchat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class SetupProfileActivity : AppCompatActivity() {

    var binding: ActivitySetupProfileBinding? = null
    var auth:FirebaseAuth? =  null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()

        val profile = intent.getStringExtra("profileImage")

        Glide.with(this@SetupProfileActivity).load(profile)
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(binding!!.profileCircleImage)

        binding!!.ivExit.setOnClickListener {
            val intent = Intent(
                this@SetupProfileActivity,
                SignUpActivity::class.java
            )
            auth!!.signOut()
            startActivity(intent)
            finish()
        }

        binding!!.ivBackspace.setOnClickListener {
            val intent = Intent(
                this@SetupProfileActivity,
                UsersActivity::class.java
            )
            startActivity(intent)
            finish()
        }

        binding!!.profileImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "profileImage/*"
            startActivityForResult(intent, 45)
        }

        binding!!.edtNameButton.setOnClickListener {
            val name: String = binding!!.edtProfileName.text.toString()
            if (name.isEmpty()) {
                binding!!.edtProfileName.error = "Please type a name"
            }
            if (selectedImage != null) {
                val reference = storage!!.reference.child("profile").child(auth!!.uid!!)
                reference.putFile(selectedImage!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reference.downloadUrl.addOnCompleteListener { uri ->
                            val imageUrl = uri.toString()
                            val uid = auth!!.uid
                            val email = auth!!.currentUser!!.email
                            val name: String = binding!!.edtProfileName.text.toString()
                            val user = User(uid, name, email, imageUrl)
                            database!!.reference
                                .child("users")
                                .child(uid!!)
                                .setValue(user)
                                .addOnCompleteListener {
                                    val intent = Intent(
                                        this@SetupProfileActivity,
                                        UsersActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }
                    }
                    else {
                        val uid = auth!!.uid
                        val email = auth!!.currentUser!!.email
                        val name: String = binding!!.edtProfileName.text.toString()
                        val user = User(uid, name, email, null)
                        database!!.reference
                            .child("users")
                            .child(uid!!)
                            .setValue(user)
                            .addOnCompleteListener {
                                val intent = Intent(this@SetupProfileActivity, UsersActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    }
                }
            } else {
                val uid = auth!!.uid
                val email = auth!!.currentUser!!.email
                val name: String = binding!!.edtProfileName.text.toString()
                val user = User(uid, name, email, null)
                database!!.reference
                    .child("users")
                    .child(uid!!)
                    .setValue(user)
                    .addOnCompleteListener {
                        val intent = Intent(this@SetupProfileActivity, UsersActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                val uri = data.data
                val storage = FirebaseStorage.getInstance()
                val time = Date().time
                val reference = storage.reference
                    .child("profile")
                    .child(time.toString() + "")
                reference.putFile(uri!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reference.downloadUrl.addOnCompleteListener { uri ->
                            val filePath = uri.toString()
                            val obj = HashMap<String, Any>()
                            obj["profileImage"] = filePath
                            database!!.reference
                                .child("users")
                                .child(FirebaseAuth.getInstance().uid!!)
                                .updateChildren(obj).addOnSuccessListener {
                                }

                        }
                    }
                }
                binding!!.profileImage.setImageURI(data.data)
                selectedImage = data.data
            }
        }
    }
}