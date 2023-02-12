package com.example.fuzzyelectric.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fuzzyelectric.FuzzyElectricApplication
import com.example.fuzzyelectric.data.FuzzyElectricRepository
import com.example.fuzzyelectric.model.Prices
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed interface FuzzyElectricUiState {
    data class Success(val prices: List<Prices>) : FuzzyElectricUiState
    object Error : FuzzyElectricUiState
    object Loading : FuzzyElectricUiState
}

class FuzzyElectricViewModel(private val fuzzyElectricRepository: FuzzyElectricRepository) :
    ViewModel() {

    var fuzzyElectricUiState: FuzzyElectricUiState by mutableStateOf(FuzzyElectricUiState.Loading)
        private set

    init {
        getPrices()
    }

    fun getPrices() {
        viewModelScope.launch {
            fuzzyElectricUiState = FuzzyElectricUiState.Loading
            fuzzyElectricUiState = try {
                FuzzyElectricUiState.Success(fuzzyElectricRepository.getPrices())
            } catch (e: IOException) {
                FuzzyElectricUiState.Error
            } catch (e: HttpException) {
                FuzzyElectricUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FuzzyElectricApplication)
                val fuzzyElectricRepository = application.container.fuzzyElectricRepository
                FuzzyElectricViewModel(fuzzyElectricRepository = fuzzyElectricRepository)
            }
        }
    }
}

