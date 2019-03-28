package com.venrique.pokedex.utils

import android.net.Uri
import android.util.Log
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class NetworkUtils {
    companion object {
        val POKEMON_API_BASE_URL = "https://pokeapi.co/api/v2/"
        private val TAG = NetworkUtils::class.java.simpleName

        fun buildUrl(pokeID: String,busqueda: String): URL {
            val builtUri = Uri.parse(POKEMON_API_BASE_URL)
                .buildUpon()
                .appendPath(busqueda)
                .appendPath(pokeID)
                .build()

            var url: URL? = null
            try {
                url = URL(builtUri.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            Log.d(TAG, "Built URI " + url!!)

            return url
        }

        @Throws(IOException::class)
        fun getResponseFromHttpUrl(url: URL): String? {
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val `in` = urlConnection.getInputStream()

                val scanner = Scanner(`in`)
                scanner.useDelimiter("\\A")

                val hasInput = scanner.hasNext()
                return if (hasInput) {
                    scanner.next()
                } else {
                    null
                }
            } finally {
                urlConnection.disconnect()
            }
        }

    }
}