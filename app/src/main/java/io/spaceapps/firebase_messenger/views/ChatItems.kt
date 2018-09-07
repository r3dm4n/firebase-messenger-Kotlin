package io.spaceapps.firebase_messenger.views

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import io.spaceapps.firebase_messenger.R
import io.spaceapps.firebase_messenger.models.User
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*


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
    }

}
