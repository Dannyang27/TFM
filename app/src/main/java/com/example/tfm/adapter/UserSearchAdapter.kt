package com.example.tfm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.diffUtil.UserDiffCallback
import com.example.tfm.model.User
import com.example.tfm.util.toBitmap
import com.example.tfm.viewHolder.UserSearchViewHolder

class UserSearchAdapter (private var users: MutableList<User>): RecyclerView.Adapter<UserSearchViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_user_search, parent, false)
        return UserSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        val user = users[position]
        holder.email = user.email
        holder.user = user.name
        holder.photoBase64 = user.profilePhoto

        holder.id = user.id.toLong()
        holder.username.text = user.name
        holder.status.text = user.status
        holder.photo

        try{
            Glide.with(holder.itemView.context).load(user.profilePhoto.toBitmap()).into(holder.photo)
        }catch (e: Exception){
            //TODO nothing
        }
    }

    override fun getItemCount() = users.size

    fun updateList( newUsers : MutableList<User>){
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(UserDiffCallback(users, newUsers))
        users.clear()
        users.addAll(newUsers)
        diffResult.dispatchUpdatesTo(this)
    }
}