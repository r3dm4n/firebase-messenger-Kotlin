package io.spaceapps.firebase_messenger.messages

import android.icu.lang.UScript
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import io.spaceapps.firebase_messenger.R
import io.spaceapps.firebase_messenger.models.User
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user?.uid
        setupNavBar()

        val adapter = GroupAdapter<ViewHolder>()


        if (user != null) {
            adapter.add(ChatToItem(user!!))
            adapter.add(ChatFromItem(user!!))
        }


        recyclerview_chat_log.adapter = adapter
    }

    private fun setupNavBar() {

    }
}

class ChatFromItem(val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_chat_from_row)
    }

}

class ChatToItem(val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_chat_to_row)
    }

}