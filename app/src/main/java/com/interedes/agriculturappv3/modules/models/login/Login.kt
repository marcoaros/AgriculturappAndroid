package com.interedes.agriculturappv3.modules.models.login

import com.google.gson.annotations.SerializedName

data class Login(@SerializedName("username")
                 var username: String? = null,
                 @SerializedName("password")
                 var password: String? = null) {
}