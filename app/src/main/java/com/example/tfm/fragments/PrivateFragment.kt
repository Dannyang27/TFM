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

class PrivateFragment : Fragment(){

    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object{
        fun newInstance(): PrivateFragment = PrivateFragment()
        lateinit var conversationList: RecyclerView
        lateinit var viewAdapter : RecyclerView.Adapter<*>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.private_chat_fragment, container, false)

        viewManager = LinearLayoutManager(activity)
        viewAdapter = ConversationAdapter(mutableListOf("test","test","test","test","test","test",
            "test","test","test","test","test","test","test"))

        conversationList = view.findViewById<RecyclerView>(R.id.private_recyclerview).apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }
}