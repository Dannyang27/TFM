package com.example.tfm.enum

import com.example.tfm.R

enum class Emoji private constructor(val titleResId: Int, val layoutResId: Int) {
    MOST_USED(R.string.most_used, R.layout.emoji_most_used),
    FACE(R.string.faces, R.layout.emoji_faces),
    ANIMAL(R.string.animal, R.layout.emoji_animal),
    FOOD(R.string.food, R.layout.emoji_food),
    SPORT(R.string.sport, R.layout.emoji_sport),
    VEHICLE(R.string.vehicle, R.layout.emoji_vehicle),
    IDEA(R.string.idea, R.layout.emoji_idea),
    CHARACTER(R.string.character, R.layout.emoji_character),
    FLAG(R.string.flag, R.layout.emoji_flag),
}