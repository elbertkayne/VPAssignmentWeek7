package com.vp.vpassignmentweek7.service

import com.vp.vpassignmentweek7.model.PokemonCardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PokemonApiService{
    @GET("cards")
    suspend fun searchCards(
        @Header("Authorization") authHeader: String,
        @Query("search") query: String
    ): Response<PokemonCardResponse>
}