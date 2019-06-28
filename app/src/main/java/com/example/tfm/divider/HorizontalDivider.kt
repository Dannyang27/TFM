package com.example.tfm.divider

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tfm.R

class HorizontalDivider(context: Context): androidx.recyclerview.widget.RecyclerView.ItemDecoration(){
    private var mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider)

    override fun onDrawOver(c : Canvas, parent: androidx.recyclerview.widget.RecyclerView, state : androidx.recyclerview.widget.RecyclerView.State){
        val left = parent.paddingLeft + 180
        val right = parent.width - (parent.paddingRight + 20)

        val childCount = parent.childCount

        repeat(childCount){
            val child = parent.getChildAt(it)
            val params = child.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight

            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }
}