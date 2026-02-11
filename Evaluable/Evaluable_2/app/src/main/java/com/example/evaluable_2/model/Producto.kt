package com.example.evaluable_2.model

class Producto (
    val id: Long? = null,
    val title: String? = null,
    val price: Double? = null,
    val images: List<String>? = null,
    val category: String? = null // para filtrar
)