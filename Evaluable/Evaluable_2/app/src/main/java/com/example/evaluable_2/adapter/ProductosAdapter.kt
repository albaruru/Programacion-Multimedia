package com.example.evaluable_2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evaluable_2.R
import com.example.evaluable_2.databinding.ProductosRecyclerBinding
import com.example.evaluable_2.model.Carrito
import com.example.evaluable_2.model.Producto
import com.google.android.material.snackbar.Snackbar

class ProductosAdapter(private val contexto: Context) :
    RecyclerView.Adapter<ProductosAdapter.MyHolder>() {

    inner class MyHolder(val binding: ProductosRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val listaProductos = ArrayList<Producto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ProductosRecyclerBinding.inflate(
            LayoutInflater.from(contexto), parent, false
        )
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val producto = listaProductos[position]

        // Muestro los datos del producto
        holder.binding.txtNombre.text = producto.title
        holder.binding.txtPrecio.text = "${producto.price} €"

        // Cargo la imagen desde URL usando Glide
        Glide.with(contexto)
            .load(producto.images?.firstOrNull())
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.binding.ImagenProducto)

        holder.binding.btnComprar.setOnClickListener {
            // Yo añado el producto al carrito centralizado
            Carrito.añadirProducto(producto)

            Snackbar.make(
                holder.binding.root,
                "Producto añadido al carrito",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = listaProductos.size

    fun setProductos(lista: List<Producto>) {
        // Yo limpio y añado los productos de manera segura
        listaProductos.clear()
        listaProductos.addAll(lista)
        notifyDataSetChanged()
    }
}