package com.venrique.pokedex

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.venrique.pokedex.models.Pokemon
import kotlinx.android.synthetic.main.lista_pokemons.view.*

class PokemonAdapter(val items: List<Pokemon>,val itemClickListener: (View, Int, Int) -> Unit): RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private var countViews: Int = 0
    private val tag = PokemonAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lista_pokemons, parent, false)

        return ViewHolder(view).onClick(itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Pokemon) = with(itemView) {
            tv_pokemon_name.text = item.nombre
        }
    }

    fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(it, getAdapterPosition(), getItemViewType())
        }
        return this
    }
}