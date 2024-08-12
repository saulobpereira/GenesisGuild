package com.genesisguild.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import kotlin.collections.HashMap

data class GameSession(
    @JsonProperty("session_id")
    val id: String,
    @JsonProperty("game_genre")
    val gameGenre: String,
    @JsonProperty("world_description")
    val worldDescription: String,
    @JsonProperty("mission")
    val mission: String,
    val startDate: LocalDateTime? = LocalDateTime.now(),
    val players: HashMap<String, Player> = HashMap(),
    val contents: HashMap<Int, Content> = HashMap(),
    val rounds: HashMap<Int, Round> = HashMap()

){
    fun addContentToHistory(content: Content){
        contents[getNextRoundIndex()] = content
    }

    fun getNextRoundIndex(): Int {
        return if(contents.isEmpty())
            0
        else
            contents.keys.last() + 1
    }
    fun canRunNextRound(): Boolean{
        var canRun = true
        players.keys.forEach { playerName ->
            canRun = canRun && rounds[getNextRoundIndex()]?.playersActions?.containsKey(playerName) == true
        }
        return canRun
    }

}
