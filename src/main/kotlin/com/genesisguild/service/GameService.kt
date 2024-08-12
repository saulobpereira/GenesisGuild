package com.genesisguild.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.genesisguild.model.*
import com.genesisguild.model.request.GeminiRequest
import com.genesisguild.model.round.RoundResponse
import com.genesisguild.repository.GameRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import java.io.File

private const val MISSION_KEY = "[[MISSION]]"

private const val PLAYERS_KEY = "[[PLAYERS]]"

private val googleApiKey = System.getenv("GOOGLE_API_KEY")

private val MODEL_URI =
    "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${googleApiKey}"

@Service
class GameService(var gameRepository: GameRepository) {

    fun saveSession(gameSession: GameSession): GameSession{
        return gameRepository.saveSession(gameSession)
    }

    fun getSession(sessionId: String): GameSession{
        return gameRepository.getSession(sessionId)
    }

    fun addPlayer(sessionId: String, player : Player): GameSession{
        return gameRepository.savePlayer(sessionId, player)
    }

    fun removePlayer(sessionId: String, player : Player): GameSession{
        return gameRepository.deletePlayer(sessionId, player)
    }

    fun deleteSession(sessionId: String): GameSession? {
        return gameRepository.deleteSession(sessionId)
    }

    fun init(sessionId: String): RoundResponse? {
        val restClient = RestClient.create()

        val geminiRequest = getBody(sessionId)
        saveContentToHistory(sessionId, geminiRequest.contents.first())

        val result = restClient.post()
            .uri(MODEL_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .body(geminiRequest)
            .retrieve()
            .toEntity<GeminiResponse>()

        println(result)

        val mapper = jacksonObjectMapper()
        val round: RoundResponse = mapper.readValue(result.body?.getText().toString())

        result.body?.candidates?.first()?.let { saveContentToHistory(sessionId, it.content) }

        println (round)
        return round
    }

    fun play(sessionId: String, playerName: String, playerAction: String): RoundResponse? {
        println("$sessionId $playerName $playerAction")
        val gameSession = gameRepository.getSession(sessionId)

        var round = gameSession.rounds[gameSession.getNextRoundIndex()]
        if (round == null) {
            round = Round()
        }
        round.playersActions[playerName] = playerAction
        gameSession.rounds[gameSession.getNextRoundIndex()] = round
        gameRepository.saveSession(gameSession)

        println("Can Run the Next Round(): ${gameSession.canRunNextRound()}")

        if(gameSession.canRunNextRound()){
            val restClient = RestClient.create()
            val geminiRequest = getRoundBody(sessionId)

            saveContentToHistory(sessionId, geminiRequest.contents.last())

            val result = restClient.post()
            .uri(MODEL_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .body(geminiRequest)
            .retrieve()
            .toEntity<GeminiResponse>()

            println(result)

            val mapper = jacksonObjectMapper()
            val round: RoundResponse = mapper.readValue(result.body?.getText().toString())

            result.body?.candidates?.first()?.let { saveContentToHistory(sessionId, it.content) }


            println (round)
            return round

        }else{
            return RoundResponse(
                players = ArrayList(),
                round_description = "",
                round_number = gameSession.getNextRoundIndex(),
                message = "Still waiting other players")
        }


    }

    fun saveContentToHistory(sessionId: String, content: Content){
        val gameSession = gameRepository.getSession(sessionId)
        gameSession.addContentToHistory(content)
        gameRepository.saveSession(gameSession)
    }

    fun getBody(sessionId: String): GeminiRequest{
        val gameSession = gameRepository.getSession(sessionId)

        val prompt = readFile("src/main/resources/prompt",
            mapOf(
                Pair(MISSION_KEY, gameSession.mission),
                Pair(PLAYERS_KEY, gameSession.playersToString())
            )
        )

        val cleanPrompt = prompt
            .replace("\r\n", "")
            .replace("\\", "")

        val request = GeminiRequest(listOf(Content(role = "user", listOf(Part(text = cleanPrompt)))))

        return request
    }
    fun getRoundBody(sessionId: String): GeminiRequest{
        val gameSession = gameRepository.getSession(sessionId)

        val playersActions = gameSession.rounds[gameSession.getNextRoundIndex()]?.playersActions

        val prompt = playersActions?.map { playerAction ->
            "The player ${playerAction.key} will ${playerAction.value}."
        }?.toString()

        val contents = ArrayList<Content>()
        contents.addAll(gameSession.contents.values)
        contents.add( Content(role = "user", listOf(Part(text = prompt.toString()))))

        val request = GeminiRequest(contents)

        return request
    }
}

fun GeminiResponse.getText() =
    this.candidates?.last()?.content?.parts?.last()?.text.toString()
        .replace("```json\n", "")
        .replace("```", "")

fun GameSession.playersToString() =
    this.players.map {
        "${it.value.name}: ${it.value.description}"
    }.toString()


fun readFile(fileName: String, parameters: Map<String, String>): String {
    var fileContent = File(fileName).readText()
    if (parameters.isNotEmpty()){
        parameters.forEach { (key, value) ->
            fileContent = fileContent.replace(key, value)
        }
    }
    return fileContent
}