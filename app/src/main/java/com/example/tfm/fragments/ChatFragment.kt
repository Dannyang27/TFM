package com.example.tfm.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfm.adapter.ChatAdapter
import com.example.tfm.divider.HorizontalDivider
import com.example.tfm.R

class ChatFragment : Fragment(){

    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object{
        fun newInstance(): ChatFragment = ChatFragment()
        lateinit var conversationList: RecyclerView
        lateinit var viewAdapter : RecyclerView.Adapter<*>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.chat_fragment, container, false)


        viewManager = LinearLayoutManager(activity)
        viewAdapter = ChatAdapter(mutableListOf("test","test","test","test","test","test","test","test"))

        conversationList = view.findViewById<RecyclerView>(R.id.chat_recyclerview).apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }
}