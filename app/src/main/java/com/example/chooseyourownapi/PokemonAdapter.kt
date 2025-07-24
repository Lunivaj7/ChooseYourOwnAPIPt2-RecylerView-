package com.example.chooseyourownapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(private val pokemonList: List<Pokemon>) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]

        // Set image
        Glide.with(holder.itemView)
            .load(pokemon.imageUrl)
            .centerCrop()
            .into(holder.pokemonImage)

        // Set name and type
        holder.pokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
        holder.pokemonType.text = "Type: ${pokemon.type.replaceFirstChar { it.uppercase() }}"
    }

    override fun getItemCount() = pokemonList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonImage: ImageView = view.findViewById(R.id.pokemon_image)
        val pokemonName: TextView = view.findViewById(R.id.pokemon_name)
        val pokemonType: TextView = view.findViewById(R.id.pokemon_type)
    }
}