package com.genesisguild.controller.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.genesisguild.model.GameSession
import java.util.*

data class SessionRequest(
    @JsonProperty("game_genre")
    val gameGenre: String,
    @JsonProperty("world_description")
    val worldDescription: String,
    @JsonProperty("mission")
    val mission: String
){
    fun getSession(): GameSession{
        return GameSession(UUID.randomUUID().toString(),gameGenre,worldDescription,mission)
    }
}
