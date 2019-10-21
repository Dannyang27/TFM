package com.example.tfm.fragments.emoji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R
import com.example.tfm.adapter.EmojiListAdapter
import com.example.tfm.enum.EmojiTab
import com.example.tfm.util.EmojiUtil

class EmojiTabFragment : Fragment() {

    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var emojiList : RecyclerView
    private lateinit var viewAdapter: EmojiListAdapter

    companion object {
        fun newInstance(emojiType: EmojiTab): EmojiTabFragment{
            val args = Bundle()
            args.putSerializable("EMOJI", emojiType)
            val fragment = EmojiTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_faces, container, false)

        val emojis = EmojiUtil.getEmojiByName(arguments?.getSerializable("EMOJI") as EmojiTab)
        gridLayoutManager = GridLayoutManager(activity, 8)
        viewAdapter = EmojiListAdapter(emojis)

        emojiList = view.findViewById<RecyclerView>(R.id.face_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = viewAdapter
        }

        return view
    }
}