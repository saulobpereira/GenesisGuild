package com.genesisguild.model

import com.fasterxml.jackson.annotation.JsonProperty

class Player(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("description")
    val description: String
)