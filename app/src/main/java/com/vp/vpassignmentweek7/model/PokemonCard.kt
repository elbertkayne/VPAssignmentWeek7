package com.vp.vpassignmentweek7.model

data class PokemonCardResponse(
    val data: List<PokemonCard>?
)

data class PokemonCard(
    val tcgPlayerId: String?,
    val name: String,
    val setName: String?,
    val cardNumber: String?,
    val rarity: String?,
    val prices: CardPrices?,
    val imageCdnUrl400: String?
)

data class CardPrices(
    val market: Double?,
    val low: Double?,
    val sellers: Int?,
    val listings: Int?,
    val primaryPrinting: String?
)