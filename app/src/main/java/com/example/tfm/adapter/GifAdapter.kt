package com.example.tfm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.tfm.R
import com.example.tfm.model.giphy.Gifs
import com.example.tfm.model.giphy.GiphyPojo
import org.jetbrains.anko.toast

class GifAdapter : BaseAdapter{

    var context: Context
    var gifs: MutableList<Gifs>

    constructor( context: Context, gifs : MutableList<Gifs>){
        this.context = context
        this.gifs = gifs
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val columns = 2
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gifView = inflater.inflate(R.layout.viewholder_gif, null)
        val width = parent?.width

        if(width?.compareTo(0)!! > 0){
            val value = width.div(columns)
            gifView.layoutParams = ViewGroup.LayoutParams(value, value / 2)
        }

        val gifImage = gifView.findViewById(R.id.gif_image) as ImageView
        val gif = gifs[position]
        Glide.with(context).asGif().load(gif.previewGif.url).centerCrop().into(gifImage)

        gifImage.setOnClickListener {
            context.toast( gif.original.url)
        }

        return gifView
    }

    override fun getItem(position: Int) = gifs[position]
    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = gifs.size
}