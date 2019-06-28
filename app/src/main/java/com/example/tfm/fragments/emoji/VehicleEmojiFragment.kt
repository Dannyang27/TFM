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

class VehicleEmojiFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance(): VehicleEmojiFragment = VehicleEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_vehicle, container, false)
        val gridview = view.findViewById(R.id.vehicle_gridview) as GridView

        gridview.adapter = EmojiGridViewAdapter(activity?.applicationContext!!, EmojiUtil.getVehicleEmoji())
        return view
    }
}