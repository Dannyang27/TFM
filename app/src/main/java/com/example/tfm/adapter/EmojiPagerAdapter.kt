package com.example.tfm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.tfm.enum.EmojiTab
import com.example.tfm.fragments.emoji.EmojiTabFragment

class EmojiPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm){

    private val mostUsedFragment = EmojiTabFragment.newInstance(EmojiTab.MOST_USED)
    private val facesFragment = EmojiTabFragment.newInstance(EmojiTab.FACE)
    private val animalFragment = EmojiTabFragment.newInstance(EmojiTab.ANIMAL)
    private val foodFragment = EmojiTabFragment.newInstance(EmojiTab.FOOD)
    private val sportFragment = EmojiTabFragment.newInstance(EmojiTab.SPORT)
    private val vehicleFragment = EmojiTabFragment.newInstance(EmojiTab.VEHICLE)
    private val ideaFragment = EmojiTabFragment.newInstance(EmojiTab.IDEA)
    private val characterEmojiFragment = EmojiTabFragment.newInstance(EmojiTab.CHARACTER)
    private val flagFragment = EmojiTabFragment.newInstance(EmojiTab.FLAG)

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> mostUsedFragment
            1 -> facesFragment
            2 -> animalFragment
            3 -> foodFragment
            4 -> sportFragment
            5 -> vehicleFragment
            6 -> ideaFragment
            7 -> characterEmojiFragment
            else -> flagFragment
        }
    }

    override fun getCount() = EmojiTab.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}