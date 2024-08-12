package com.genesisguild.controller.request

import com.fasterxml.jackson.annotation.JsonProperty

data class PlayerOptionRequest(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("action")
    val action: String
)
