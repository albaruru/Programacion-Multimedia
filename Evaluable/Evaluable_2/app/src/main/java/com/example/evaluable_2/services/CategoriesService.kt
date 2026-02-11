package com.example.evaluable_2.services

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.evaluable_2.adapter.SpinnerAdapter
import com.example.evaluable_2.model.Categoria

class CategoriesService(val contexto: Context) {

    /**
     * En este método me encargo de pedir las categorías a la API.
     * No devuelvo nada directamente, sino que actualizo el SpinnerAdapter
     * cuando la petición termina correctamente.
     */
    fun getCategorias(spinnerAdapter: SpinnerAdapter) {
        val url = "https://dummyjson.com/products/categories"
        val request = JsonArrayRequest(
            url,
            { response ->
                val listaCategorias = ArrayList<Categoria>()

                for (item in 0 until response.length()) {
                    val categoriaObj = response.getJSONObject(item)
                    val slug = categoriaObj.getString("slug")
                    val name = categoriaObj.getString("name")
                    val url = categoriaObj.optString("url", null)

                    listaCategorias.add(Categoria(slug, name, url))
                }

                // Actualizar el SpinnerAdapter con las categorías
                spinnerAdapter.setCategorias(listaCategorias)
            },
            { error ->
                Log.e("JSON Categorias", "Error: ${error.message}")
            }
        )

        Volley.newRequestQueue(contexto).add(request)
    }
}