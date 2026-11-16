package com.vp.vpassignmentweek7.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.vpassignmentweek7.model.PokemonCard
import com.vp.vpassignmentweek7.repository.PokemonRepository
import kotlinx.coroutines.launch

sealed interface UiState {
    object Idle : UiState
    object Loading : UiState
    data class Success(val cards: List<PokemonCard>) : UiState
    data class Error(val message: String) : UiState
}

class PokemonViewModel (private val repository: PokemonRepository): ViewModel(){
    var uiState: UiState by mutableStateOf(UiState.Idle)
        private set

    var searchQuery by mutableStateOf("Charizard")
        private set

    var selectedCard by mutableStateOf<PokemonCard?>(null)
        private set

    init {
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun searchCards(query: String) {
        uiState = UiState.Loading
        viewModelScope.launch {
            val result = repository.searchCards(query)
            uiState = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { val errorMessage = if (it.message?.contains("429") == true) {
                    "Terlalu banyak permintaan (Rate Limit 429). Tunggu beberapa menit."
                } else {
                    it.message ?: "Unknown error"
                }
                    UiState.Error(errorMessage)
                }
            )
        }
    }

    fun selectCard(card: PokemonCard?) {
        selectedCard = card
    }
}
