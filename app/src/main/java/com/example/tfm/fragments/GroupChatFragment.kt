package com.example.tfm.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfm.R

class GroupChatFragment : androidx.fragment.app.Fragment(){

    companion object{
        fun newInstance(): GroupChatFragment = GroupChatFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.groupchat_fragment, container, false)

        return view
    }
}

