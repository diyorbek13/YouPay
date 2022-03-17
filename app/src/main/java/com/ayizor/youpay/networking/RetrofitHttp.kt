package com.ayizor.youpay.networking

import com.ayizor.youpay.networking.service.CardService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHttp {
    companion object {
        const val IS_TESTER = false

        const val SERVER_DEVELOPMENT = "https://623302de6de3467dbac5cccc.mockapi.io/"
        const val SERVER_PRODUCTION = "https://623302de6de3467dbac5cccc.mockapi.io/"

        private fun server(): String {
            if (IS_TESTER) {
                return SERVER_DEVELOPMENT
            }
            return SERVER_PRODUCTION
        }

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(server())
                .build()
        }

        val cardService= getRetrofit().create(CardService::class.java)
    }
}