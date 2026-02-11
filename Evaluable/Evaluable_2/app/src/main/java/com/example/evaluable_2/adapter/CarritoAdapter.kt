package com.example.evaluable_2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evaluable_2.databinding.CompraRecyclerBinding
import com.example.evaluable_2.model.Carrito
import com.example.evaluable_2.model.Producto
import com.google.android.material.snackbar.Snackbar

class CarritoAdapter(
    private val contexto: Context,
    private val onCambioCarrito: () -> Unit  // Callback para notificar cambios
) : RecyclerView.Adapter<CarritoAdapter.MyHolder>() {

    private val listaCarrito = ArrayList<Producto>()

    inner class MyHolder(val binding: CompraRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = CompraRecyclerBinding.inflate(
            LayoutInflater.from(contexto), parent, false
        )
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = listaCarrito[position]

        holder.binding.txtNombre.text = producto.title
        holder.binding.txtPrecio.text = String.format("%.2f €", producto.price ?: 0.0)

        Glide.with(contexto)
            .load(producto.images?.firstOrNull())
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(holder.binding.ImagenProductoC)

        // Botón eliminar con callback
        holder.binding.btnEliminar.setOnClickListener {
            val posicionActual = holder.adapterPosition
            if (posicionActual != RecyclerView.NO_POSITION) {
                // Eliminar del carrito global
                Carrito.getProductos().removeAt(posicionActual)

                // Eliminar de la lista local
                listaCarrito.removeAt(posicionActual)
                notifyItemRemoved(posicionActual)

                // Notificar a la Activity para actualizar el total
                onCambioCarrito()

                Snackbar.make(
                    holder.binding.root,
                    "Producto eliminado del carrito",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int = listaCarrito.size

    fun setProductos(productos: List<Producto>) {
        listaCarrito.clear()
        listaCarrito.addAll(productos)
        notifyDataSetChanged()
    }

    fun clearProductos() {
        listaCarrito.clear()
        notifyDataSetChanged()
    }

    fun getTotal(): Double = listaCarrito.sumOf { it.price ?: 0.0 }
}