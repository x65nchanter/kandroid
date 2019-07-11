package org.x65nchanter.kandroid.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.x65nchanter.kandroid.data.Board
import org.x65nchanter.kandroid.services.BoardService

@RestController
@RequestMapping("board")
class BoardController(val boardService: BoardService) {

    val logger = LoggerFactory.getLogger(BoardController::class.java)

    @GetMapping
    fun index() = boardService.getAll()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody board: Board): Board {
        val result = boardService.add(board)
        logger.debug(result.toString())
        return result
    }

//    @GetMapping("{id}")
//    @ResponseStatus(HttpStatus.FOUND)
//    fun read(@PathVariable id: Long) = boardService.get(id)
//
//    @PutMapping("{id}")
//    fun update(@PathVariable id: Long, @RequestBody board: Board) = boardService.edit(id, board)
//
//    @DeleteMapping("{id}")
//    fun delete(@PathVariable id: Long) = boardService.remove(id)
}