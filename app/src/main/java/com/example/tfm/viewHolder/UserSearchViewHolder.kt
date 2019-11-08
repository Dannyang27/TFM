package com.example.tfm.viewHolder

import android.view.View
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.tfm.R
import de.hdodenhof.circleimageview.CircleImageView

class UserSearchViewHolder(view : View) : RecyclerView.ViewHolder(view){

    @BindView(R.id.user_photo) lateinit var photo: CircleImageView
    @BindView(R.id.user_username) lateinit var username: EmojiTextView
    @BindView(R.id.user_status) lateinit var status: EmojiTextView

    var id: Long = -1
    lateinit var email: String
    lateinit var user: String
    lateinit var photoBase64: String

    init {
        ButterKnife.bind(this, view)
    }
}