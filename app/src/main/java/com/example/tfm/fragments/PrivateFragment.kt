package com.example.tfm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfm.adapter.ConversationAdapter
import com.example.tfm.divider.HorizontalDivider
import com.example.tfm.R
import com.example.tfm.model.Conversation
import com.example.tfm.room.database.MyRoomDatabase
import com.google.firebase.auth.FirebaseAuth

class PrivateFragment : Fragment(){

    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object{
        fun newInstance(): PrivateFragment = PrivateFragment()
        lateinit var conversationRecyclerView: RecyclerView
        lateinit var viewAdapter : ConversationAdapter
        lateinit var conversations : MutableList<Conversation>

        fun addConversation( conversation: Conversation){
            conversations.add(conversation)
            viewAdapter.notifyDataSetChanged()
        }

        fun updateConversation(newConversations: MutableList<Conversation>){
            viewAdapter.updateList(newConversations)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_private_chat, container, false)

        viewManager = LinearLayoutManager(this.activity)

        conversations = mutableListOf()
        viewAdapter = ConversationAdapter(conversations)

        conversationRecyclerView = view.findViewById<RecyclerView>(R.id.private_recyclerview).apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val roomDatabase = MyRoomDatabase.getMyRoomDatabase(activity?.applicationContext!!)
//        roomDatabase?.deleteAllConversation()
        roomDatabase?.showAllConversationInLog()
        roomDatabase?.showAllUserInLog()

        roomDatabase?.loadUserConversation(FirebaseAuth.getInstance().currentUser?.email!!)

        return view
    }
}