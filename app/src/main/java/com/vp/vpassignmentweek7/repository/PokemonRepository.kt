package com.vp.vpassignmentweek7.repository

import com.vp.vpassignmentweek7.model.PokemonCard
import com.vp.vpassignmentweek7.service.PokemonApiService

class PokemonRepository(private val apiService: PokemonApiService) {
    private val APIKey = "pokeprice_free_0f8b936145fcf1bb3a30071a5bd7a32a24a2dcfb7087bb44"

    suspend fun searchCards(query: String): Result<List<PokemonCard>> {
        return try {
            val response = apiService.searchCards("Bearer $APIKey", query)
            if (response.isSuccessful) {
                val body = response.body()
                Result.success(body?.data ?: emptyList())
            } else {
                Result.failure(Exception("API request failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}