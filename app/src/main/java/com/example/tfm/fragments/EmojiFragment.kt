package com.example.tfm.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.tfm.R
import com.example.tfm.adapter.EmojiPagerAdapter
import com.example.tfm.enum.TabIcon
import com.google.android.material.tabs.TabLayout

class EmojiFragment : Fragment(){

    private lateinit var emojiTabs: TabLayout
    private lateinit var specialKeyboard: ViewPager

    companion object{
        fun newInstance(): EmojiFragment = EmojiFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_emoji, container, false)
        emojiTabs = view.findViewById(R.id.emoji_tab)
        specialKeyboard = view.findViewById(R.id.emoji_keyboard)

        activity?.let {
            val fragmentAdapter = EmojiPagerAdapter(it.supportFragmentManager)
            specialKeyboard.adapter = fragmentAdapter
            emojiTabs.setupWithViewPager(specialKeyboard)
            setupTabIcons(it)
        }

        return view
    }

    private fun setupTabIcons(ctx : Context){
        emojiTabs.getTabAt(0)?.icon = ctx.getDrawable(TabIcon.MOST_USED.icon)
        emojiTabs.getTabAt(1)?.icon = ctx.getDrawable(TabIcon.FACES.icon)
        emojiTabs.getTabAt(2)?.icon = ctx.getDrawable(TabIcon.ANIMAL.icon)
        emojiTabs.getTabAt(3)?.icon = ctx.getDrawable(TabIcon.FOOD.icon)
        emojiTabs.getTabAt(4)?.icon = ctx.getDrawable(TabIcon.SPORT.icon)
        emojiTabs.getTabAt(5)?.icon = ctx.getDrawable(TabIcon.VEHICLE.icon)
        emojiTabs.getTabAt(6)?.icon = ctx.getDrawable(TabIcon.IDEA.icon)
        emojiTabs.getTabAt(7)?.icon = ctx.getDrawable(TabIcon.CHARACTER.icon)
        emojiTabs.getTabAt(8)?.icon = ctx.getDrawable(TabIcon.FLAG.icon)
    }
}