package com.example.tfm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.GridView
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import com.example.tfm.R
import com.example.tfm.adapter.GifAdapter
import com.example.tfm.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GifFragment : Fragment(), CoroutineScope{

    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    companion object{
        fun newInstance(): GifFragment = GifFragment()
        lateinit var adapter: ListAdapter
        var gifImages = mutableListOf<String>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gif, container, false)

        val gridview = view.findViewById(R.id.gif_gridview) as GridView
        adapter = GifAdapter(activity?.applicationContext!!, gifImages)
        gridview.adapter = adapter

        launch {
            RetrofitClient.getSearchGifFromGiphy("pugs")
        }

        return view
    }
}