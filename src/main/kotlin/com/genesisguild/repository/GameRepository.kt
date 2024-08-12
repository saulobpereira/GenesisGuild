package com.genesisguild.repository

import com.genesisguild.model.GameSession
import com.genesisguild.model.Player
import org.springframework.stereotype.Repository

@Repository
class GameRepository {
    private val sessions = HashMap<String, GameSession>()

    fun saveSession(session: GameSession): GameSession{
        println("Saving session: $session")
        sessions[session.id] = session
        return session
    }

    fun getSession(sessionId: String) : GameSession {
        return sessions[sessionId]!!
    }

    fun savePlayer(sessionId: String, player: Player): GameSession{
        val session: GameSession = sessions[sessionId]!!
        session.players[player.name] = player
        sessions[sessionId] = session
        return session
    }

    fun deletePlayer(sessionId: String, player: Player): GameSession{
        val session: GameSession = sessions[sessionId]!!
        session.players.remove(player.name)
        sessions[sessionId] = session
        return session
    }

    fun deleteSession(sessionId: String): GameSession? {
        return sessions.remove(sessionId)
    }
}