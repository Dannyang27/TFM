package com.example.tfm.fragments.emoji

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfm.R

class SportEmojiFragment : Fragment() {

    companion object {
        fun newInstance(): SportEmojiFragment = SportEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_sport, container, false)

        return view
    }
}