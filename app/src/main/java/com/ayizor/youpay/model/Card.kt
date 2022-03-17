package com.ayizor.youpay.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Card(
    @SerializedName("id")
    @Expose val id: String = "",
    @SerializedName("cvv")
    @Expose val cvv: String = "",
    @SerializedName("card_number")
    @Expose val card_number: String = "",
    @SerializedName("expiration_data")
    @Expose val expiration_data: String,
    @SerializedName("holder_name")
    @Expose val holder_name: String? = null,
    @SerializedName("synchronized")
    @Expose val synchronized: Int
)