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

class GroupChatFragment : Fragment(){
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object{
        fun newInstance(): GroupChatFragment = GroupChatFragment()
        lateinit var groups: RecyclerView
        lateinit var viewAdapter : RecyclerView.Adapter<*>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_groupchat, container, false)

        viewManager = LinearLayoutManager(this.activity)
        viewAdapter = ConversationAdapter(mutableListOf())

        groups = view.findViewById<RecyclerView>(R.id.group_recyclerview).apply {
            setHasFixedSize(true)
            addItemDecoration(HorizontalDivider(this.context))
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }
}

