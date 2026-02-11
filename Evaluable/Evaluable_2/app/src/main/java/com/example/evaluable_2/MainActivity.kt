package com.example.evaluable_2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.evaluable_2.adapter.ProductosAdapter
import com.example.evaluable_2.adapter.SpinnerAdapter
import com.example.evaluable_2.databinding.ActivityMainBinding
import com.example.evaluable_2.model.Producto
import com.example.evaluable_2.services.CategoriesService
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    // ViewBinding me permite acceder a las vistas sin usar findViewById
    private lateinit var binding: ActivityMainBinding
    // Adapter del RecyclerView que muestra los productos
    private lateinit var adaptador: ProductosAdapter
    // Adapter propio para el Spinner de categorías
    private lateinit var spinnerAdapter: SpinnerAdapter
    // Servicio encargado de traer las categorías desde la API
    private lateinit var categoriesService: CategoriesService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuro la Toolbar como ActionBar
        setSupportActionBar(binding.menu)

        instancias() // Inicializo adaptadores y servicios
        configurarRecycler() // Configuro RecyclerView
        configurarSpinner() // Configuro Spinner con categorías y listener
        cargarProductos() // Traigo los productos desde el JSON
    }

    /**
     * Aquí creo todas las instancias necesarias.
     * Lo separo en un método para que el onCreate sea más limpio
     * y fácil de leer.
     */
    private fun instancias() {
        adaptador = ProductosAdapter(this)
        spinnerAdapter = SpinnerAdapter(this) // Instancio mi clase propia
        categoriesService = CategoriesService(this)
    }

    /**
     * Configuro el RecyclerView indicando:
     * - Que quiero una lista vertical
     * - Qué adapter va a gestionar los datos
     */
    private fun configurarRecycler() {
        binding.recyclerProductos.layoutManager = LinearLayoutManager(this)
        binding.recyclerProductos.adapter = adaptador
    }

    /**
     * Aquí configuro el Spinner de categorías.
     * 1. Asigno el adapter
     * 2. Cargo las categorías desde la API
     * 3. Escucho cuándo el usuario selecciona una categoría
     */
    private fun configurarSpinner() {
        binding.CategoriaSpinner.adapter = spinnerAdapter.adapterSpinner

        // Cargar categorías
        categoriesService.getCategorias(spinnerAdapter)
        binding.CategoriaSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    if (position == 0) {
                        // "Selecciona:" seleccionado - cargar todos
                        cargarProductos(null)
                    } else {
                        // Obtener slug de la categoría seleccionada
                        val slug = spinnerAdapter.getSlug(position - 1)
                        Log.d("SPINNER", "Posición: $position, Slug: $slug")
                        cargarProductos(slug) // ← Cargar por categoría
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    cargarProductos(null)
                }
            }
    }

    /**
     * Este método se encarga de cargar productos desde la API.
     * - Si no hay categoría, cargo todos los productos
     * - Si hay categoría, llamo al endpoint específico
     * Uso Volley para la petición y Gson para convertir JSON a objetos Producto
     */
    private fun cargarProductos(categoria: String? = null) {
        // Si hay categoría, usar endpoint específico, sino todos los productos
        val url = if (categoria != null) {
            "https://dummyjson.com/products/category/$categoria"
        } else {
            "https://dummyjson.com/products?limit=0"
        }

        Log.d("PRODUCTOS", "Cargando desde: $url")

        val request = JsonObjectRequest(url, { response ->
            val gson = Gson()
            val array = response.getJSONArray("products")

            val listaProductos = ArrayList<Producto>()

            for (i in 0 until array.length()) {
                val producto = gson.fromJson(
                    array.getJSONObject(i).toString(),
                    Producto::class.java
                )
                listaProductos.add(producto)
            }
            // Actualizo el RecyclerView con los nuevos productos
            adaptador.setProductos(listaProductos)

            Log.d("PRODUCTOS", "Cargados ${listaProductos.size} productos para categoría: $categoria")

        }, { error ->
            Log.e("PRODUCTOS", "Error al cargar productos: ${error.message}")
            adaptador.setProductos(emptyList()) // Limpiar en caso de error
        })

        Volley.newRequestQueue(this).add(request)
    }

    // Inflar menú de la Toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Gestionar clicks del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.carritoCompra -> {
                startActivity(Intent(this, SecondActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
