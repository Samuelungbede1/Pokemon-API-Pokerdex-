package com.example.goheme.Functions

object Functions {
    fun getUrlNum(url: String) : Int{
        val splited = url.split("/")
        return splited[splited.lastIndex-1].toInt()
    }

    fun returnImageUrl (name:String): String{
        return  "https://img.pokemondb.net/artwork/large/${name}.jpg"
    }
}