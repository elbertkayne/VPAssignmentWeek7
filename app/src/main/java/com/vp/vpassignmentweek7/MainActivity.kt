package com.vp.vpassignmentweek7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vp.vpassignmentweek7.repository.PokemonRepository
import com.vp.vpassignmentweek7.service.PokemonApiService
import com.vp.vpassignmentweek7.view.PokemonScreen
import com.vp.vpassignmentweek7.viewmodel.PokemonViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.pokemonpricetracker.com/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(PokemonApiService::class.java)
        val repository = PokemonRepository(apiService)

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return PokemonViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val viewModel = ViewModelProvider(this, viewModelFactory)[PokemonViewModel::class.java]

        setContent {
            PokemonScreen(viewModel = viewModel)
        }
    }
}