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
import org.x65nchanter.kandroid.data.Column
import org.x65nchanter.kandroid.services.ColumnService

@RestController
@RequestMapping("column")
class ColumnController(val columnService: ColumnService) {
    //TODO: Логирование
    val logger: Logger = LoggerFactory.getLogger(ColumnController::class.java)


    @GetMapping
    fun index() = columnService.getAll()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody column: Column) = columnService.add(column)

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun read(@PathVariable id: Long) = columnService.get(id)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody column: Column) = columnService.edit(id, column)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = columnService.remove(id)
}