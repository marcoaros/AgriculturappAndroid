package com.interedes.agriculturappv3.asistencia_tecnica.models

import com.google.gson.annotations.SerializedName
import com.interedes.agriculturappv3.config.DataSource
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = DataSource::class)
data class Tratamiento(@PrimaryKey(autoincrement = true)
                       @SerializedName("Id")
                       @Column(name = "Id")
                       var Id: Long? = 0,
                       @SerializedName("Categoria_InsumoId")
                       @Column(name = "Categoria_InsumoId")
                       var Categoria_InsumoId: Long? = 0,
                       @SerializedName("Desc_Aplicacion")
                       @Column(name = "Desc_Aplicacion")
                       var Desc_Aplicacion: String? = null,
                       @SerializedName("Desc_Formulacion")
                       @Column(name = "Desc_Formulacion")
                       var Desc_Formulacion: String? = null,
                       @SerializedName("IngredienteActivo")
                       @Column(name = "IngredienteActivo")
                       var IngredienteActivo: String? = null,
                       @SerializedName("InsumoId")
                       @Column(name = "InsumoId")
                       var InsumoId: Long? = 0,
                       @SerializedName("Modo_Accion")
                       @Column(name = "Modo_Accion")
                       var Modo_Accion: String? = null,
                       @SerializedName("Nombre_Comercial")
                       @Column(name = "Nombre_Comercial")
                       var Nombre_Comercial: String? = null,
                       @SerializedName("precioAproximado")
                       @Column(name = "precioAproximado")
                       var precioAproximado: Double? = 0.0,
                       @SerializedName("proveedor")
                       @Column(name = "proveedor")
                       var proveedor: String? = null) {
}