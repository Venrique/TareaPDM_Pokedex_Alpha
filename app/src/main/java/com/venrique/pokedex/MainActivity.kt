package com.venrique.pokedex

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import com.venrique.pokedex.utils.NetworkUtils
import android.os.AsyncTask
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.venrique.pokedex.models.Pokemon
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lista_pokemons.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException



class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val tag = MainActivity::class.java.simpleName

    var mPokemonNumber: EditText? = null
    var mSearchButton: Button? = null
    var mPokemonName: TextView? = null
    var pokeType: TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()

        mSearchButton?.setOnClickListener { view ->
            val pokemonNumber = mPokemonNumber?.text.toString().trim()
            if (!pokemonNumber.isEmpty()) {
                FetchPokemonTask().execute(pokemonNumber)
            }

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }



    fun initRecycler(pokes: List<String>) {

        val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->

            val intent: Intent = Intent(this,PokemonInfo::class.java)
            intent.putExtra("pokeNombre",view.tv_pokemon_name.text.toString())
            startActivity(intent)
            Log.d(tag,view.tv_pokemon_name.text.toString())
        }

        var pokemon: MutableList<Pokemon> = MutableList(pokes.size-1) { i ->
            Pokemon(i,pokes[i])
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = PokemonAdapter(pokemon,itemOnClick)

        rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    private fun bindView() {
        mPokemonNumber = findViewById(R.id.et_pokemon_number)
        mSearchButton = findViewById(R.id.bt_search_pokemon)
        mPokemonName = findViewById(R.id.tv_pokemon_name)
        pokeType = findViewById(R.id.tv_pokemon_type)
    }

    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg pokemonNumbers: String): String? {

            if (pokemonNumbers.isEmpty()) {
                return null
            }

            val ID = pokemonNumbers[0]

            val pokeAPI = NetworkUtils.buildUrl(ID,"type")

            try {
                return NetworkUtils.getResponseFromHttpUrl(pokeAPI)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(pokemonInfo: String?) {
            if (pokemonInfo != null && pokemonInfo != "") {
                var jsonPokemons = JSONObject(pokemonInfo)
                var pokeArray = jsonPokemons.getJSONArray("pokemon")

                var listaP = mutableListOf<String>()

                for (i in 0..pokeArray.length()-1) listaP.add(i,pokeArray.getJSONObject(i).getJSONObject("pokemon").getString("name"))

                pokeType?.setText(jsonPokemons.getString("name"))
                initRecycler(listaP)
                //pokeArray.getJSONObject(0).getJSONObject("pokemon").getString("name")
            } else {
                var listaP = mutableListOf<String>()
                listaP.add(0,"No se encontraron pokemons")
                initRecycler(listaP)
            }
        }
    }

}
