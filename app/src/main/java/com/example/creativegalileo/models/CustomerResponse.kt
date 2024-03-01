package com.example.creativegalileo.models

import com.google.gson.annotations.SerializedName

data class CustomerResponse(
    @SerializedName("success") val success : Boolean,
    @SerializedName("date") val date : String,
    @SerializedName("data") val customerData: CustomerData
)


data class CustomerData (
    @SerializedName("count") val count : Int,
    @SerializedName("customers") val customers : List<Customer>
)
