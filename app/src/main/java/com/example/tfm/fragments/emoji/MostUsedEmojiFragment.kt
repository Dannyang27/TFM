package com.example.tfm.fragments.emoji

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.example.tfm.R
import com.example.tfm.adapter.EmojiGridViewAdapter
import com.example.tfm.util.EmojiUtil

class MostUsedEmojiFragment : Fragment() {

    companion object {
        fun newInstance(): MostUsedEmojiFragment = MostUsedEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_most_used, container, false)
        val gridview = view.findViewById(R.id.mostused_gridview) as GridView

        gridview.adapter = EmojiGridViewAdapter(activity?.applicationContext!!, EmojiUtil.getEmojiMostUsed())


        return view
    }
}