package com.example.chooseyourownapi

import android.os.Bundle
import android.util.Log
//import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private lateinit var pokemonList: MutableList<Pokemon>

    private lateinit var rvPokemons: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        rvPokemons = findViewById<RecyclerView>(R.id.pokemon_list)
        pokemonList = mutableListOf()
        fetchPokemonImages()

    }


    private fun fetchPokemonImages() {
        val client = AsyncHttpClient()
        val url = "https://pokeapi.co/api/v2/pokemon?limit=20"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val resultsArray = json.jsonObject.getJSONArray("results")
                pokemonList = mutableListOf()
                val adapter = PokemonAdapter(pokemonList)
                rvPokemons.adapter = adapter
                rvPokemons.layoutManager = LinearLayoutManager(this@MainActivity)

                for (i in 0 until resultsArray.length()) {
                    val pokemonObject = resultsArray.getJSONObject(i)
                    val name = pokemonObject.getString("name")
                    val detailUrl = pokemonObject.getString("url")

                    client.get(detailUrl, object : JsonHttpResponseHandler() {
                        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                            val spriteUrl = json.jsonObject
                                .getJSONObject("sprites")
                                .getString("front_default")

                            val typesArray = json.jsonObject.getJSONArray("types")
                            val typeObject = typesArray.getJSONObject(0).getJSONObject("type")
                            val typeName = typeObject.getString("name")

                            pokemonList.add(Pokemon(name, spriteUrl, typeName))
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String,
                            throwable: Throwable?
                        ) {
                            Log.e("Pokemon Detail Error", response)
                        }
                    })
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String, throwable: Throwable?) {
                Log.e("Pokemon Error", response)
            }
        })
    }



}