package com.example.tfm.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.tfm.enum.EmojiTab
import com.example.tfm.fragments.emoji.*
import com.example.tfm.util.EmojiUtil

class EmojiPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm){

    private val mostUsedFragment = MostUsedEmojiFragment.newInstance()
    private val facesFragment = FacesEmojiFragment.newInstance()
    private val animalFragment = AnimalEmojiFragment.newInstance()
    private val foodFragment = FoodEmojiFragment.newInstance()
    private val sportFragment = SportEmojiFragment.newInstance()
    private val vehicleFragment = VehicleEmojiFragment.newInstance()
    private val ideaFragment = IdeaEmojiFragment.newInstance()
    private val characterEmojiFragment = CharacterEmojiFragment.newInstance()
    private val flagFragment = FlagEmojiFragment.newInstance()

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

//    override fun getPageTitle(position: Int): CharSequence? {
//        return when(position){
//            0 -> EmojiUtil.getEmojiUnicode(0x1F600)
//
//            1 -> EmojiUtil.getEmojiUnicode(0x1F600)
//
//            2 -> EmojiUtil.getEmojiUnicode(0x1F600)
//
//            3 -> EmojiUtil.getEmojiUnicode(0x1F600)
//
//            4 -> EmojiUtil.getEmojiUnicode(0x1F600)
//
//            5 -> EmojiUtil.getEmojiUnicode(0x1F600)
//
//            6 -> EmojiUtil.getEmojiUnicode(0x1F600)
//
//            7 -> EmojiUtil.getEmojiUnicode(0x1F600)
//
//            else -> EmojiUtil.getEmojiUnicode(0x1F600)
//        }
//    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}