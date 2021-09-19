package com.example.goheme.Ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goheme.Functions.Functions
import com.example.goheme.R
import com.example.goheme.model.Pokemon

class PokemonAdapter(var contex: Context, var data: ArrayList<Pokemon>, val listener: OnItemClickListener): RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {


    class ViewHolder (val view: View): RecyclerView.ViewHolder(view) {
        var photoView: ImageView = view.findViewById(R.id.iv_PokemonImage)
        var textView: TextView =  view.findViewById(R.id.tv_PokemonName)
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        var view : View = LayoutInflater.from(parent.context).inflate(R.layout.poke_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentPokemon : Pokemon = data[position]
        holder.textView.setText(currentPokemon.name)

        val currentUrl = data[position].url
        var currentUrlNum = Functions.getUrlNum(currentUrl!!)
        currentPokemon.pokemonId = currentUrlNum


        /** Using this glide to load image into the image view, and the following line sets the
         * text view to the name of the current pokemon displayed*/

        Glide.with(contex).load("https://img.pokemondb.net/artwork/large/${currentPokemon.name}.jpg")
            .into(holder.photoView)
        holder.textView.text = currentPokemon.name



        /**Setting click listener to the item in the recyclerview*/
        holder.view.setOnClickListener {
            listener.OnItemClick(currentPokemon)
        }
    }



    /**This function returns the size of the elements in the recylcer view*/
    override fun getItemCount(): Int {
        return data.size
    }


    /**This function adds pokemon to the recycler view*/
    fun addittionalListOfPokemon ( listOfPokemon: ArrayList<Pokemon>) {
        data.addAll(listOfPokemon)
        notifyDataSetChanged()
    }


    /**The interface class for the on click listener, the function(s) inside
     * should be overridden in the class implementing this class*/
    interface OnItemClickListener {
        fun OnItemClick(pokemon: Pokemon)
    }
}