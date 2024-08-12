package com.genesisguild.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.genesisguild.controller.request.PlayerOptionRequest
import com.genesisguild.controller.request.PlayerRequest
import com.genesisguild.controller.request.SessionRequest
import com.genesisguild.controller.response.SessionResponse
import com.genesisguild.controller.response.createSessionResponseFrom
import com.genesisguild.model.round.RoundResponse
import com.genesisguild.service.GameService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/game")
class GameController(var gameService: GameService) {
    private val mapper = jacksonObjectMapper()

    @PostMapping("/session")
    fun createSession(@RequestBody sessionRequest: SessionRequest): SessionResponse {
        return createSessionResponseFrom( gameService.saveSession(sessionRequest.getSession()))
    }

    @GetMapping("/session/{id}")
    fun getSession(@PathVariable id: String): SessionResponse {
        return createSessionResponseFrom( gameService.getSession(id))
    }

    @PostMapping("/session/{id}/player")
    fun createPlayer(@PathVariable id: String, @RequestBody playerRequest: PlayerRequest): SessionResponse {
        return createSessionResponseFrom( gameService.addPlayer(id, playerRequest.getPlayer()))
    }

    @DeleteMapping("/session/{id}/player")
    fun removePlayer(@PathVariable id: String, @RequestBody playerRequest: PlayerRequest): SessionResponse {
        return createSessionResponseFrom( gameService.removePlayer(id, playerRequest.getPlayer()))
    }

    @PostMapping("/session/{id}/init")
    fun initSession(@PathVariable id: String): RoundResponse? {
        return gameService.init(id)
    }

    @PostMapping("/session/{id}/play")
    fun playSession(@PathVariable id: String, @RequestBody playerOptionRequest: PlayerOptionRequest): RoundResponse? {
        return gameService.play(id, playerOptionRequest.name, playerOptionRequest.action )
    }

    @DeleteMapping("/session/{id}")
    fun deleteSession(@PathVariable id: String): SessionResponse? {
        return gameService.deleteSession(id)?.let { createSessionResponseFrom(it) }
    }
}