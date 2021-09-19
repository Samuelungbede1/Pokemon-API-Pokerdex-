package com.example.goheme.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goheme.PokemonDetailPage
import com.example.goheme.R
import com.example.goheme.Util.BASE_URL
import com.example.goheme.model.ConnectivityLiveData
import com.example.goheme.model.Pokemon
import com.example.goheme.model.PokemonRequest
import com.example.goheme.service.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), PokemonAdapter.OnItemClickListener {

    var TAG = "POKEDEX"

    /** Declaring variables*/
    private lateinit var retrofit: Retrofit
    private lateinit var progressBar: ProgressBar
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var numberOfPokemons: EditText
    private lateinit var fetchButton: Button
   // private lateinit var offsetNum: Int
    var listOfPokemon =  ArrayList<Pokemon>()

   // private lateinit var networkObject: PokeApiService



    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Assigning variables*/
        connectivityLiveData = ConnectivityLiveData(application)
        recyclerView = findViewById(R.id.rv_RecyclerView)
        nextButton = findViewById(R.id.btn_next)
        fetchButton = findViewById(R.id.btn_fetch)
        previousButton = findViewById(R.id.btn_previous)
        progressBar = findViewById(R.id.pb_ProgressBar)
        numberOfPokemons = findViewById(R.id.et_NumberOfPokemon)
        pokemonAdapter = PokemonAdapter(this, listOfPokemon, this)
        recyclerView.adapter = PokemonAdapter(this,listOfPokemon,this)
        recyclerView.setHasFixedSize(true)
        var layoutManager = GridLayoutManager(this,2)
        recyclerView.layoutManager = layoutManager

       var offsetNum = 0
        var limitNum = 2


        /**Creating and building the retrofit*/
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        /** Next Button*/
        nextButton.setOnClickListener {
            limitNum= numberOfPokemons.text.toString().toInt()
            offsetNum+=limitNum
            obtainData(limitNum,offsetNum)
        }

        /** Previous button*/
        previousButton.setOnClickListener {
            if(offsetNum ==0){
                Toast.makeText(this, "Press next to see more Pokemons", Toast.LENGTH_SHORT).show()
            } else {
                offsetNum -= limitNum
                obtainData(limitNum, offsetNum)
            }
        }

        /** Fetch button*/
        fetchButton.setOnClickListener {
               limitNum= numberOfPokemons.text.toString().toInt()
               obtainData(limitNum, offsetNum)
        }



        /**Connectivity live data observer*/
        connectivityLiveData.observe(this, Observer { isAvailable ->
            when (isAvailable) {
                true -> {
                    Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    obtainData(limitNum,offsetNum)
                }
                false -> {
                    Toast.makeText(this, "Please put ON your internet", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
        })
    }


         /**Function that obtains the data from the API as a response*/
        private fun obtainData (limit: Int, offset: Int) {
        retrofit.create(PokeApiService::class.java)
            .getProperties(limit,offset)
            .enqueue(object: Callback<PokemonRequest>{

            override fun onResponse (call: Call<PokemonRequest>, response: Response<PokemonRequest>) {
                if(response.isSuccessful){
                    var pokemonRequest: PokemonRequest = response.body()!!
                    var listOfPokemon: ArrayList<Pokemon> = pokemonRequest.results
                    pokemonAdapter.addittionalListOfPokemon(listOfPokemon)
                    recyclerView.adapter =  PokemonAdapter(this@MainActivity,listOfPokemon,this@MainActivity)
                } else{
                    Log.e(TAG, "onResponse: "+ response.errorBody())
                }
            }
            override fun onFailure(call: Call<PokemonRequest>, t: Throwable) {
                Log.e(TAG, "onFailure: "+ t.message)
            }
        })
    }


    /**Setting click listener to an item on the recycler view*/
    override fun OnItemClick(pokemon: Pokemon) {
        val intent= Intent(this, PokemonDetailPage::class.java)
        intent.putExtra("name", pokemon.name)
        startActivity(intent)
    }
}