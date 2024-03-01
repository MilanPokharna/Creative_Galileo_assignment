package com.example.creativegalileo.models

import com.google.gson.annotations.SerializedName

data class Customer (

    @SerializedName("id") val id : String,
    @SerializedName("cgId") val cgId : Long,
    @SerializedName("name") val name : String,
    @SerializedName("mobile") val mobile : Long,
    @SerializedName("dialCode") val dialCode : String,
    @SerializedName("email") val email : String,
    @SerializedName("recordStatus") val recordStatus : Boolean,
    @SerializedName("createdAt") val createdAt : String,
    @SerializedName("updatedAt") val updatedAt : String
)
