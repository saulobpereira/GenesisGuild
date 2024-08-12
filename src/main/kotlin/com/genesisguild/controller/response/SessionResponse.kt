package com.genesisguild.controller.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.genesisguild.model.Content
import com.genesisguild.model.GameSession
import com.genesisguild.model.Player
import com.genesisguild.model.Round
import java.time.LocalDateTime

data class SessionResponse (
    @JsonProperty("session_id")
    val id: String,
    @JsonProperty("game_genre")
    val gameGenre: String,
    @JsonProperty("world_description")
    val worldDescription: String,
    @JsonProperty("mission")
    val mission: String,
    @JsonProperty("start_date")
    val startDate: LocalDateTime?,
    @JsonProperty("players")
    val players: HashMap<String, Player> = HashMap(),
    @JsonProperty("contents")
    val contents: HashMap<Int, Content> = HashMap(),
    @JsonProperty("rounds")
    val rounds: HashMap<Int, Round> = HashMap()
)
fun createSessionResponseFrom(session : GameSession): SessionResponse {
    return SessionResponse(session.id ,session.gameGenre,session.worldDescription,session.mission, session.startDate, session.players, session.contents, session.rounds)
}