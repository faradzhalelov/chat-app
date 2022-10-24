package com.dzhalelov.android.chatchat.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dzhalelov.android.chatchat.R
import com.dzhalelov.android.chatchat.databinding.DeleteLayoutBinding
import com.dzhalelov.android.chatchat.databinding.SendMsgBinding
import com.dzhalelov.android.chatchat.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MessageAdapter(
    var context: Context,
    messages: ArrayList<Message>?,
    senderRoom: String,
    receiverRoom: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>()
{

    lateinit var messages: ArrayList<Message>

    val ITEM_SENT = 1
    val ITEM_RECEIVE = 1
    val senderRoom: String
    val receiverRoom: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM_SENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.send_msg,
                parent, false)
            SentMsgHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.send_msg,
                parent, false)
            ReceiveMsgHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (FirebaseAuth.getInstance().uid == message.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messages[position]
        if (holder.javaClass == SentMsgHolder:: class.java) {
            val viewHolder = holder as SentMsgHolder
            if (message.message.equals("profileImage")) {
                viewHolder.binding.ivImage.visibility = View.VISIBLE
                viewHolder.binding.tvSendMessage.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(viewHolder.binding.ivImage)
            }
            viewHolder.binding.tvSendMessage.text = message.message
            viewHolder.itemView.setOnClickListener {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.delete_layout, null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.tvEveryone.setOnClickListener {
                    message.message = "This is message is removed"
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1!!).setValue(message)
                    }
                    message.messageId.let { it1 ->
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(receiverRoom)
                                .child("message")
                                .child(it1!!).setValue(message)
                    }
                    dialog.dismiss()
                }
                binding.tvDelete.setOnClickListener {
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }
                binding.tvCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        } else {
            val viewHolder = holder as ReceiveMsgHolder
            if (message.message.equals("profileImage")) {
                viewHolder.binding.ivImage.visibility = View.VISIBLE
                viewHolder.binding.tvSendMessage.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(viewHolder.binding.ivImage)
            }
            viewHolder.binding.tvSendMessage.text = message.message
            viewHolder.itemView.setOnClickListener {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.delete_layout, null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()

                binding.tvEveryone.setOnClickListener {
                    message.message = "This is message is removed"
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1!!).setValue(message)
                    }
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(it1!!).setValue(message)
                    }
                    dialog.dismiss()
                }
                binding.tvDelete.setOnClickListener {
                    message.messageId.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1!!).setValue(null)
                    }
                    dialog.dismiss()
                }
                binding.tvCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }

    }

    override fun getItemCount(): Int = messages.size

    inner class SentMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SendMsgBinding = SendMsgBinding.bind(itemView)
    }

    inner class ReceiveMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SendMsgBinding = SendMsgBinding.bind(itemView)
    }

    init {
        if (messages != null) {
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }
}