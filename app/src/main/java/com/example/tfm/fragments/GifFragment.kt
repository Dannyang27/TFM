package com.example.tfm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.tfm.R
import com.example.tfm.adapter.GifAdapter
import com.example.tfm.model.giphy.Gifs
import com.example.tfm.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView
import com.example.tfm.util.KeyboardUtil

class GifFragment : Fragment(), CoroutineScope{

    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    companion object{
        fun newInstance(): GifFragment = GifFragment()
        lateinit var adapter: GifAdapter
        var gifImages = mutableListOf<Gifs>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gif, container, false)

        val gridview = view.findViewById(R.id.gif_gridview) as GridView
        val searchView = view.findViewById(R.id.gif_searchview) as SearchView

        adapter = GifAdapter(activity?.applicationContext!!, gifImages)
        gridview.adapter = adapter

        launch {
            RetrofitClient.getGifsFromGiphy()
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                KeyboardUtil.hideKeyboard(activity!!)
                launch {
                    RetrofitClient.getSearchGifFromGiphy(query!!)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    searchView.clearFocus()
                }
                return true
            }
        })

        return view
    }
}