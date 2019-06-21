package com.example.tfm.fragments.emoji

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.example.tfm.R
import com.example.tfm.adapter.EmojiGridViewAdapter

class FacesEmojiFragment : Fragment() {

    companion object {
        fun newInstance(): FacesEmojiFragment = FacesEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_faces, container, false)

        val gridview = view.findViewById<GridView>(R.id.face_gridview)

        gridview.adapter = EmojiGridViewAdapter(activity?.applicationContext!!, setSampleData())

        return view
    }

    private fun setSampleData(): ArrayList<String>{
        val list = arrayListOf<String>()
        repeat(60){
            list.add("a")
        }

        return list
    }
}