package com.ayizor.youpay.networking.service

import com.ayizor.youpay.model.Card
import retrofit2.Call
import retrofit2.http.*

interface CardService {

    @GET("cards")
    fun getAllCards(): Call<List<Card>>

    @GET("cards/{id}")
    fun getCard(@Path("id") id: String): Call<List<Card>>

    @POST("cards")
    fun createCard(@Body card: Card): Call<Card>

}