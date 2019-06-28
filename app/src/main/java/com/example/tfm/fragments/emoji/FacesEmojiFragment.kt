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

class FacesEmojiFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance(): FacesEmojiFragment = FacesEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_faces, container, false)

        val gridview = view.findViewById(R.id.face_gridview) as GridView

        gridview.adapter = EmojiGridViewAdapter(activity?.applicationContext!!, EmojiUtil.getEmojiFaces())

        return view
    }
}