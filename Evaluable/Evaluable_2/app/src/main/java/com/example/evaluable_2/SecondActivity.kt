package com.example.evaluable_2

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evaluable_2.adapter.CarritoAdapter
import com.example.evaluable_2.databinding.ActivitySecondBinding
import com.example.evaluable_2.model.Carrito
import com.google.android.material.snackbar.Snackbar

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var adaptador: CarritoAdapter

    // Configuro la Toolbar con botón de volver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // IDs del LAYOUT (activity_second.xml)
        setSupportActionBar(binding.toolbarCarrito)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        configurarRecycler()
        cargarCarrito()
    }

    // Configuro el RecyclerView del carrito
    private fun configurarRecycler() {
        adaptador = CarritoAdapter(this) { actualizarTotal() }
        // IDs del LAYOUT (activity_second.xml)
        binding.recyclerCarrito.layoutManager = LinearLayoutManager(this)
        binding.recyclerCarrito.adapter = adaptador
    }

    // Cargo los productos directamente desde el objeto Carrito
    private fun cargarCarrito() {
        adaptador.setProductos(Carrito.getProductos())
        actualizarTotal()
    }

    // Calculo y muestro el total del carrito
    private fun actualizarTotal() {
        val total = Carrito.getTotal()
        // IDs del LAYOUT (activity_second.xml)
        binding.totalCompra.text = String.format("Total: %.2f €", total)
    }

    override fun onResume() {
        super.onResume()
        cargarCarrito()
    }

    // Infla el MENÚ (second_menu.xml)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.second_menu, menu)
        return true
    }

    // IDs del MENÚ (second_menu.xml)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.item_confirmar -> {  // ← ID del second_menu.xml
                if (Carrito.getProductos().isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        "El carrito está vacío",
                        Snackbar.LENGTH_SHORT).show()
                } else {
                    val total = Carrito.getTotal()
                    Snackbar.make(
                        binding.root,
                        String.format("Enhorabuena, compra por valor de %.2f € realizada", total),
                        Snackbar.LENGTH_LONG
                    ).show()
                    Carrito.vaciar()
                    cargarCarrito()
                }
                true
            }
            R.id.item_vaciar -> {  // ← ID del second_menu.xml
                if (Carrito.getProductos().isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        "El carrito ya está vacío",
                        Snackbar.LENGTH_SHORT).show()
                } else {
                    Carrito.vaciar()
                    cargarCarrito()
                    Snackbar.make(
                        binding.root,
                        "Carrito vaciado",
                        Snackbar.LENGTH_SHORT).show()
                }
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}