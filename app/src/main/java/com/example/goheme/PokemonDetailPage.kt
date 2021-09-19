package com.example.goheme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.goheme.Functions.Functions
import com.example.goheme.model.PokemonDataClass.Details
import com.example.goheme.model.ConnectivityLiveData
import com.example.goheme.service.NetworkClient
import com.example.goheme.service.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "mainActivity"

class PokemonDetailPage : AppCompatActivity() {


    /**Declaring variables*/
    lateinit var heightDetail: TextView
    lateinit var weightDetail: TextView
    lateinit var moveDetail: TextView
    lateinit var imageDetails: ImageView
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var DetailsprogressBar: ProgressBar
    private lateinit var retrofit: Retrofit
    private lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail_page)

        /** Assigning variables*/
        heightDetail = findViewById(R.id.tv_height)
        weightDetail = findViewById(R.id.tv_weight)
        moveDetail = findViewById(R.id.tv_moves)
        imageDetails = findViewById(R.id.iv_imageDetails)
        DetailsprogressBar = findViewById(R.id.pb_DetailsProgressBar)
        connectivityLiveData = ConnectivityLiveData(application)

        Log.d(TAG, "onCreate instance")
        name = intent.getStringExtra("name").toString()

        /** Using glide to load image to the image view*/

        Glide
            .with(this)
            .load(Functions.returnImageUrl(name))
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(imageDetails);


        /** Creating the retrofit and build it*/
        retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/pokemon/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        NetworkClient.retrofitBuilder()

        connectivityLiveData.observe(this, Observer { isAvailable ->
            when (isAvailable) {
                true -> {

                    DetailsprogressBar.visibility = View.GONE
                    heightDetail.visibility = View.VISIBLE
                    weightDetail.visibility = View.VISIBLE
                    moveDetail.visibility = View.VISIBLE
                    imageDetails.visibility = View.VISIBLE
                    obtainPokemonDetails()
                }
                false -> {
                    DetailsprogressBar.visibility = View.VISIBLE
                    heightDetail.visibility = View.GONE
                    weightDetail.visibility = View.GONE
                    moveDetail.visibility = View.GONE
                    imageDetails.visibility = View.GONE
                }
            }
        })


    }


    /** This function collects the pokemon details as a response*/
    private fun obtainPokemonDetails() {
        val retroBuilder = retrofit.create(PokeApiService::class.java)
        val retroResponse = retroBuilder.getPokemonDetails(name)
        retroResponse.enqueue(object : Callback<Details> {

            override fun onResponse(call: Call<Details>, response: Response<Details>) {

                var detailsReturned = response.body()
                if (detailsReturned != null) {
                    heightDetail.text = String.format("Height : ${detailsReturned.height} ft")
                    weightDetail.text = String.format("Weigth : ${detailsReturned.weight} pounds")
                    moveDetail.text = String.format(
                        "Move Details: ${detailsReturned.moves[0].move.name},${detailsReturned.moves[1].move.name}," +
                                "${detailsReturned.moves[2].move.name}"
                    )
                }
                Log.d("SOMETHING", "${response.body()}")
                println("RESULT: ${response.body()?.name}")
                println("RESULT:${response.body()?.height}")
            }


            override fun onFailure(call: Call<Details>, t: Throwable) {
                Log.d("SOMETHING", "on request failed")
            }
        })
    }
}