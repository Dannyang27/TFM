package com.example.tfm.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.tfm.model.User

class UserDiffCallback(private val oldUsers: MutableList<User>, private val newUsers: MutableList<User>) : DiffUtil.Callback(){
    override fun getOldListSize() = oldUsers.size
    override fun getNewListSize() = newUsers.size
    override fun areItemsTheSame(oldPosition: Int, newPosition: Int) = oldUsers[oldPosition].email == newUsers[newPosition].email
    override fun areContentsTheSame(oldPosition: Int, newPosition: Int) = oldUsers[oldPosition] == newUsers[newPosition]
}