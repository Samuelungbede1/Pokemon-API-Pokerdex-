package com.example.goheme.model.PokemonDataClass

import com.google.gson.annotations.SerializedName

data class GenerationI(
    @SerializedName("red-blue")
    val red_blue: RedBlue,
    val yellow: Yellow
)