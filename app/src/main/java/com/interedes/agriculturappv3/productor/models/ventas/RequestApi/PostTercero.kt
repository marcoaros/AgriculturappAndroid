package com.interedes.agriculturappv3.productor.models.ventas.RequestApi

import com.google.gson.annotations.SerializedName

data class PostTercero(
                       @SerializedName("Id")
                       var Id: Long? = 0,

                       @SerializedName("Nombre")
                       var Nombre: String? = null,

                       @SerializedName("Apellido")
                       var Apellido: String? = null,

                       @SerializedName("NITRut")
                       var NitRut: String? = null) {
}