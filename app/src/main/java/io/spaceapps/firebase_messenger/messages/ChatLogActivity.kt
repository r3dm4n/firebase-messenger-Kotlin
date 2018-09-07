package io.spaceapps.firebase_messenger.messages

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import io.spaceapps.firebase_messenger.R
import io.spaceapps.firebase_messenger.models.ChatMessage
import io.spaceapps.firebase_messenger.models.User
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLogActivity"
    }

    private var toUser: User? = null
    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerview_chat_log.adapter = adapter

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username


        listenForMessages()
        send_button_chatlog.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages() {

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@ChatLogActivity, p0.message, Toast.LENGTH_SHORT).show()
                return
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestMessagesActivity.currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }

                    scrollToBottom()
                }
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }


            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })

    }

    private fun performSendMessage() {
        val text = edittext_chat_log.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        if (fromId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)

        reference.setValue(chatMessage)
                .addOnSuccessListener {
                    edittext_chat_log.text.clear()

                }

        toReference.setValue(chatMessage)

        scrollToBottom()
    }

    private fun scrollToBottom() {
        recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)

    }
}


class ChatFromItem(val text: String, val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int = R.layout.chat_from_row

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_chat_from_row)
    }

}

class ChatToItem(val text: String, val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int = R.layout.chat_to_row


    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text

        val targetImageView = viewHolder.itemView.imageview_chat_to_row
        Picasso.get().load(user.profileImageUrl).into(targetImageView)
        Log.d("TAG", user.profileImageUrl)
    }

}

