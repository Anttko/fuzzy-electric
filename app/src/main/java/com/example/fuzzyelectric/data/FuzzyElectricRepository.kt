package com.example.fuzzyelectric.data

import com.example.fuzzyelectric.model.Prices
import com.example.fuzzyelectric.network.FuzzyElectricApiService

interface FuzzyElectricRepository {
    suspend fun getPrices(): List<Prices>
}

class NetworkFuzzyElectricRepository(
    private val fuzzyElectricApiService: FuzzyElectricApiService
) : FuzzyElectricRepository {
    override suspend fun getPrices(): List<Prices> = fuzzyElectricApiService.getPrices()
}
