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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MostUsedEmojiFragment : Fragment(), CoroutineScope {

    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    companion object {
        fun newInstance(): MostUsedEmojiFragment = MostUsedEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_most_used, container, false)
        val gridview = view.findViewById(R.id.mostused_gridview) as GridView

        launch {
            loadGridview(gridview, EmojiUtil.getEmojiMostUsed())
        }

        return view
    }
}