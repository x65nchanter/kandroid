package org.x65nchanter.kandroid.controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.x65nchanter.kandroid.services.BoardService
import java.util.UUID

@RestController
@RequestMapping("board")
class BoardController(val boardService: BoardService) {

    val logger: Logger? = LoggerFactory.getLogger(BoardController::class.java)

    @GetMapping
    fun index() =
            boardService.index()
                    .also { logger?.info("Return board index list") }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody board: BoardService.InputCreateBoard) =
            boardService.create(board)
                    .also { logger?.info("Created board: $it") }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun read(@PathVariable id: UUID) =
            boardService.read(id)
                    .also { logger?.info("Found board: $it") }

    @PutMapping("{id}")
    fun update(@PathVariable id: UUID, @RequestBody board: BoardService.InputBoard) =
            boardService.update(id, board)
                    .also { logger?.info("Updated board from: $board to: $it ") }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) =
            boardService.delete(id)
                    .also { logger?.info("Deleted board id: $id") }
}