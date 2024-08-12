package com.genesisguild.controller.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.genesisguild.model.Player

data class PlayerRequest(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("description")
    val description: String
){
    fun getPlayer(): Player{
        return Player(name, description)
    }
}
