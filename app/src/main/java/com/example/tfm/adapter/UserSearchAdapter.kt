package com.example.tfm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.model.User
import com.example.tfm.viewHolder.UserSearchViewHolder

class UserSearchAdapter (private val users: MutableList<User>): RecyclerView.Adapter<UserSearchViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_user_search, parent, false)
        return UserSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        val user = users[position]
        holder.username.text = user.name
        holder.status.text = user.status
    }

    override fun getItemCount() = users.size
}