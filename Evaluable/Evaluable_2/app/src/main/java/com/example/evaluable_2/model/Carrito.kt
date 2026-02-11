package com.example.evaluable_2.model

object Carrito {

    // Uso un object para tener un carrito único en toda la app
    private val productos = ArrayList<Producto>()

    fun añadirProducto(producto: Producto) {
        productos.add(producto)
    }

    fun vaciar() {
        productos.clear()
    }

    fun getProductos(): ArrayList<Producto> {
        return productos
    }

    fun getTotal(): Double {
        // calculo aquí el total para no duplicar lógica
        return productos.sumOf { it.price ?: 0.0 }
    }
}
