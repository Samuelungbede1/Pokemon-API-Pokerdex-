package com.example.goheme.model.PokemonDataClass

import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    val black_white: BlackWhite
)