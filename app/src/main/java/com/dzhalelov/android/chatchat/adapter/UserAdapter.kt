package com.dzhalelov.android.chatchat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dzhalelov.android.chatchat.R
import com.dzhalelov.android.chatchat.activity.ChatActivity
import com.dzhalelov.android.chatchat.databinding.ItemUserBinding
import com.dzhalelov.android.chatchat.model.User
import com.google.android.material.imageview.ShapeableImageView

class UserAdapter(var context: Context, var userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.tvUserName.text = user.name

        Glide.with(context).load(user.profileImage)
            .placeholder(R.drawable.avatar)
            .into(holder.binding.profileImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("profileImage", user.profileImage)
            intent.putExtra("uid", user.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = userList.size

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding: ItemUserBinding = ItemUserBinding.bind(itemView)

    }
}