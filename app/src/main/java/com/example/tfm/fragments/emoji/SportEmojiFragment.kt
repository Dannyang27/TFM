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
import kotlinx.coroutines.*

class SportEmojiFragment : Fragment(), CoroutineScope {

    override val coroutineContext get() = Dispatchers.Default

    companion object {
        fun newInstance(): SportEmojiFragment = SportEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_sport, container, false)
        val gridview = view.findViewById(R.id.sport_gridview) as GridView

        launch {
            loadGridview(gridview)
        }

        return view
    }

    private suspend fun loadGridview( gridview : GridView){
        coroutineScope {
            async {
                withContext(Dispatchers.IO){
                    val adapter = EmojiGridViewAdapter(activity?.applicationContext!!, EmojiUtil.getSportEmoji())
                    withContext(Dispatchers.Main){
                        gridview.adapter = adapter
                    }
                }
            }
        }
    }
}