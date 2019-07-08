package com.example.tfm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tfm.R
import com.example.tfm.retrofit.RetrofitClient

class GifFragment : Fragment(){

    companion object{
        fun newInstance(): GifFragment = GifFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gif, container, false)
        RetrofitClient.getGifsFromGiphy()

        return view
    }
}