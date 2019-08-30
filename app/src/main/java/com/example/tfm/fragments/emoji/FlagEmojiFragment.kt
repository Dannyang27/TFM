package com.example.tfm.fragments.emoji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.tfm.R
import com.example.tfm.adapter.EmojiFlagAdapter
import com.example.tfm.util.EmojiUtil
import kotlinx.coroutines.*

class FlagEmojiFragment : Fragment(), CoroutineScope {

    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    companion object {
        fun newInstance(): FlagEmojiFragment = FlagEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_flag, container, false)
        val gridview = view.findViewById(R.id.flag_gridview) as GridView

        launch {
            loadGridview(gridview)
        }

        return view
    }

    private suspend fun loadGridview( gridview : GridView){
        launch{
            withContext(Dispatchers.IO){
                val adapter = EmojiFlagAdapter(activity?.applicationContext!!, EmojiUtil.getFlagEmoji())
                withContext(Dispatchers.Main){
                    gridview.adapter = adapter
                }
            }
        }
    }
}