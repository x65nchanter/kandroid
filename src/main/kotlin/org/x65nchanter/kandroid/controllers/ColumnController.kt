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
import org.x65nchanter.kandroid.services.ColumnService
import java.util.UUID

@RestController
@RequestMapping("column")
class ColumnController(val columnService: ColumnService) {
    //TODO:
    val logger: Logger? = LoggerFactory.getLogger(ColumnController::class.java)


    @GetMapping
    fun index() =
            columnService.index()
                    .also { logger?.info("Return column index list") }

    @PostMapping("board/{boardId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody column: ColumnService.InputColumn, @PathVariable boardId: UUID) =
            columnService.create(column, boardId)
                    .also { logger?.info("Created column: $it") }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun read(@PathVariable id: UUID) =
            columnService.read(id)
                    .also { logger?.info("Found column: $it") }

    @PutMapping("{id}")
    fun update(@PathVariable id: UUID, @RequestBody column: ColumnService.InputColumn) =
            columnService.update(id, column)
                    .also { logger?.info("Updated column from: $column to: $it ") }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) =
            columnService.delete(id)
                    .also { logger?.info("Deleted column id: $id") }
}