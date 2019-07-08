package com.example.tfm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.tfm.R
import org.jetbrains.anko.toast

class GifAdapter : BaseAdapter{

    var context: Context
    var gifs: MutableList<String>

    constructor( context: Context, gifs : MutableList<String>){
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

        val gif = gifView.findViewById(R.id.gif_image) as ImageView
        gif.setOnClickListener {
            context.toast("clicked")
        }

        Glide.with(context).asGif().load(gifs[position]).centerCrop().into(gif)

        return gifView
    }

    override fun getItem(position: Int) = gifs[position]
    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = gifs.size
}