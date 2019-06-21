package com.example.tfm.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfm.enum.Emoji

class CustomPagerAdapter(private val context: Context) : PagerAdapter(){

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val modelObject = Emoji.values()[position]
        val layout = LayoutInflater.from(context)
            .inflate(modelObject.layoutResId, container, false) as ViewGroup
        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, p1: Any): Boolean {
        return view === p1
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val customPagerEnum = Emoji.values()[position]
        return context.getString(customPagerEnum.titleResId)
    }

    override fun getCount(): Int = Emoji.values().size
}