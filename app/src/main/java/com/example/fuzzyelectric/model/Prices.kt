package com.example.fuzzyelectric.model

import kotlinx.serialization.Serializable

@Serializable
data class Prices(

    /*   val datetime: String,
       val pricewithtax: Double,
       val pricewithouttax: Double,*/

    val DateTime: String,
    val PriceNoTax: Double,
    val PriceWithTax: Double,
    val Rank: Integer,
)

internal object Compare {
    fun min(a: Prices, b: Prices): Prices {
        return if (a.PriceWithTax < b.PriceWithTax ) a else b
    }
    fun max(a: Prices, b: Prices): Prices {
        return if (a.PriceWithTax > b.PriceWithTax ) a else b
    }

}
