package com.example.fuzzyelectric.network

import com.example.fuzzyelectric.model.Prices
import retrofit2.http.GET

interface FuzzyElectricApiService {
    @GET("Today")
    suspend fun getPrices(): List<Prices>
}