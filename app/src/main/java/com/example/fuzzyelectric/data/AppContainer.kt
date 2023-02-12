package com.example.fuzzyelectric.data

import com.example.fuzzyelectric.network.FuzzyElectricApiService
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

interface AppContainer {
    val fuzzyElectricRepository: FuzzyElectricRepository
}



class DefaultAppContainer: AppContainer {
    private val BASE_URL =
        "https://api.spot-hinta.fi/"
    @kotlinx.serialization.ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()
    private val retrofitService: FuzzyElectricApiService by lazy {
        retrofit.create(FuzzyElectricApiService::class.java)
    }
    override val fuzzyElectricRepository: FuzzyElectricRepository by lazy {
        NetworkFuzzyElectricRepository(retrofitService)
    }


}
