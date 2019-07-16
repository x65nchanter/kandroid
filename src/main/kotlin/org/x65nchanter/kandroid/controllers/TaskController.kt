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
import org.x65nchanter.kandroid.data.Task
import org.x65nchanter.kandroid.services.TaskService

@RestController
@RequestMapping("task")
class TaskController(val taskService: TaskService) {
    //TODO: Логирование
    val logger: Logger = LoggerFactory.getLogger(TaskController::class.java)


    @GetMapping
    fun index() = taskService.getAll()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody task: Task) = taskService.add(task)

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun read(@PathVariable id: Long) = taskService.get(id)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody task: Task) = taskService.edit(id, task)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = taskService.remove(id)
}