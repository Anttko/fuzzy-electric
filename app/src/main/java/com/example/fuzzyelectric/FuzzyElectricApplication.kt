package com.example.fuzzyelectric

import android.app.Application
import com.example.fuzzyelectric.data.AppContainer
import com.example.fuzzyelectric.data.DefaultAppContainer

class FuzzyElectricApplication: Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }

}