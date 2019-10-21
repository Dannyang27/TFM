package com.example.tfm.fragments.emoji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.tfm.R
import com.example.tfm.util.EmojiUtil
import com.example.tfm.util.loadGridview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FacesEmojiFragment : Fragment() {

    companion object {
        fun newInstance(): FacesEmojiFragment = FacesEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_faces, container, false)
        val gridview = view.findViewById(R.id.face_gridview) as GridView

        CoroutineScope(Dispatchers.IO).launch{
            loadGridview(gridview, EmojiUtil.getEmojiFaces())
        }

        return view
    }
}