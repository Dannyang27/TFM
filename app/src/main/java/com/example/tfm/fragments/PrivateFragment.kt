package com.example.tfm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.adapter.ConversationAdapter
import com.example.tfm.divider.HorizontalDivider
import com.example.tfm.model.Conversation

class PrivateFragment : Fragment(){
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter : ConversationAdapter

    companion object{
        fun newInstance(): PrivateFragment = PrivateFragment()
    }

    fun updateList(conversations: MutableList<Conversation>){
        viewAdapter.updateList(conversations)
    }

    fun updateAdapter(position: Int){
        viewAdapter.notifyItemChanged(position)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_private_chat, container, false)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = ConversationAdapter(mutableListOf())

        recyclerView = view.findViewById<RecyclerView>(R.id.private_recyclerview).apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }
}