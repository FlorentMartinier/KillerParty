package com.fmartinier.killerparty.client

import retrofit2.Call
import retrofit2.http.POST

interface KillerBackClient {

    @POST("session/create")
    fun createSession(): Call<Any>
}