package com.genesisguild.model.round

import com.fasterxml.jackson.annotation.JsonProperty

data class RoundResponse(
    @JsonProperty("players")
    val players: List<Player>,
    @JsonProperty("round_description")
    val round_description: String,
    @JsonProperty("round_number")
    val round_number: Int,
    @JsonProperty("message")
    val message: String = ""
)