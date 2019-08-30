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

class VehicleEmojiFragment : Fragment(), CoroutineScope {

    private val job = Job()
    override val coroutineContext get() = Dispatchers.Default + job

    companion object {
        fun newInstance(): VehicleEmojiFragment = VehicleEmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.emoji_vehicle, container, false)
        val gridview = view.findViewById(R.id.vehicle_gridview) as GridView

        launch {
            loadGridview(gridview, EmojiUtil.getVehicleEmoji())
        }

        return view
    }
}