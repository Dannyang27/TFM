package com.example.tfm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tfm.R
import com.example.tfm.adapter.GifAdapter
import com.example.tfm.model.giphy.Gifs
import com.example.tfm.util.KeyboardUtil
import com.example.tfm.viewmodel.GifViewModel

class GifFragment : Fragment(){

    private lateinit var adapter: GifAdapter
    private lateinit var gifViewModel: GifViewModel
    private val gifs = mutableListOf<Gifs>()

    companion object{
        fun newInstance(): GifFragment = GifFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gif, container, false)
        val gridview = view.findViewById(R.id.gif_gridview) as GridView
        val searchView = view.findViewById(R.id.gif_searchview) as SearchView

        gifViewModel = ViewModelProviders.of(activity!!).get(GifViewModel::class.java)
        gifViewModel.getGifs().observe(activity!!, Observer { newGifs ->
            gifs.clear()
            gifs.addAll(newGifs)
            adapter.notifyDataSetChanged()
        })

        adapter = GifAdapter(activity?.applicationContext!!, gifs)
        gridview.adapter = adapter

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                KeyboardUtil.hideKeyboard(activity!!)
                gifViewModel.searchGiphyGifs(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()){
                    searchView.clearFocus()
                }
                return true
            }
        })

        gifViewModel.getTrendyGiphyGifs()
        return view
    }
}