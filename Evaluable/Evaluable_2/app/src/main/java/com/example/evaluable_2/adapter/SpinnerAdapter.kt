package com.example.evaluable_2.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.example.evaluable_2.model.Categoria

class SpinnerAdapter(contexto: Context) {

    private val listadoCategorias = ArrayList<Categoria>() // Lista de objetos Categoria

    // ArrayAdapter de String que el Spinner mostrará
    val adapterSpinner: ArrayAdapter<String> = ArrayAdapter(
        contexto,
        android.R.layout.simple_spinner_item,
        ArrayList<String>() // inicializo vacío, luego se llena con nombres
    ).apply {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    /**
     * Aquí actualizo completamente el contenido del Spinner.
     * Mantengo sincronizadas:
     * - La lista de objetos Categoria
     * - La lista de Strings que se muestra al usuario
     */
    fun setCategorias(listaCategorias: List<Categoria>) {
        listadoCategorias.clear()
        listadoCategorias.addAll(listaCategorias)

        adapterSpinner.clear()
        adapterSpinner.add("Selecciona:") // Primer elemento
        adapterSpinner.addAll(listaCategorias.map { it.name ?: "Sin nombre" })
        adapterSpinner.notifyDataSetChanged()
    }

    /**
     * Este método me permite obtener el slug real
     * a partir de la posición seleccionada en el Spinner
     */
    fun getSlug(position: Int): String? {
        return listadoCategorias.getOrNull(position)?.slug
    }
}