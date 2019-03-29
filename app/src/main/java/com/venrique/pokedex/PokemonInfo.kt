package com.venrique.pokedex

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.venrique.pokedex.utils.NetworkUtils
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException

class PokemonInfo : AppCompatActivity() {

    var infoPokeName: TextView?= null
    var infoPokeInfo: TextView?= null
    var infoPokePeso: TextView?= null
    var infoPokeAltura: TextView?= null
    var infoPokeTipo: TextView?=null
    var infoImagen: ImageView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_info)

        infoPokeName = findViewById(R.id.tv_pokeinfo_name)
        infoPokeInfo = findViewById(R.id.info)
        infoPokePeso = findViewById(R.id.peso)
        infoPokeAltura = findViewById(R.id.altura)
        infoPokeTipo = findViewById(R.id.tipos)
        infoImagen = findViewById(R.id.imagen)

        val intent: Intent = getIntent()

        infoPokeName?.setText(intent.getStringExtra("pokeNombre"))

        FetchPokemonTask().execute(intent.getStringExtra("pokeNombre"))

    }

    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg pokemonNumbers: String): String? {

            if (pokemonNumbers.isEmpty()) {
                return null
            }

            val ID = pokemonNumbers[0]

            val pokeAPI = NetworkUtils.buildUrl(ID,"pokemon")

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
                var pokeArray = jsonPokemons.getJSONArray("abilities")
                var pokeType = jsonPokemons.getJSONArray("types")


                var listaP = mutableListOf<String>()

                var listaT = mutableListOf<String>()

                infoImagen?.let {
                    Glide.with(this@PokemonInfo).load(jsonPokemons.getJSONObject("sprites").getString("front_default")).into(
                        it
                    )
                }

                for (i in 0..pokeArray.length()-1) listaP.add(i,pokeArray.getJSONObject(i).getJSONObject("ability").getString("name"))

                for(e in 0..pokeType.length()-1) listaT.add(e,pokeType.getJSONObject(e).getJSONObject("type").getString("name"))

                infoPokeInfo?.setText(listaP.toString())
                infoPokePeso?.setText((jsonPokemons.getDouble("weight")/10.0).toString() + " Kg")
                infoPokeAltura?.setText((jsonPokemons.getDouble("height")/10.0).toString() +" m")
                infoPokeTipo?.setText(listaT.toString())
                //pokeArray.getJSONObject(0).getJSONObject("pokemon").getString("name")
            } else {
                infoPokeInfo?.setText("Nada que mostrar")
            }
        }
    }
}
