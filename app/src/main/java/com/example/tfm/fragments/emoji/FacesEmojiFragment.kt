package com.example.tfm.fragments.emoji

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.example.tfm.R
import com.example.tfm.adapter.EmojiGridViewAdapter
import com.example.tfm.model.Emoji

class FacesEmojiFragment : Fragment() {

    companion object {
        fun newInstance(): FacesEmojiFragment = FacesEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_faces, container, false)

        val gridview = view.findViewById(R.id.face_gridview) as GridView

        gridview.adapter = EmojiGridViewAdapter(activity?.applicationContext!!, setSampleData())

        return view
    }

    private fun setSampleData(): ArrayList<Emoji>{
        val list = arrayListOf<Emoji>()
        repeat(374){
            list.add(Emoji(Drawable.createFromStream(activity?.assets?.open("emojifaces/white-frowning-face_2639.png"), null), getEmojiUnicode(0x1F4A2)))
        }

        return list
    }

    private fun getEmojiUnicode(unicode: Int): String{
        return String(Character.toChars(unicode))
    }
}