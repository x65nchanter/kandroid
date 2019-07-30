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
import org.x65nchanter.kandroid.services.TaskService
import java.util.UUID

@RestController
@RequestMapping("task")
class TaskController(val taskService: TaskService) {
    val logger: Logger? = LoggerFactory.getLogger(TaskController::class.java)


    @GetMapping
    fun index() =
            taskService.index()
                    .also { logger?.info("Return task index list") }

    @PostMapping("column/{columnId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody task: TaskService.InputCreateTask, @PathVariable columnId: UUID) =
            taskService.create(task, columnId)
                    .also { logger?.info("Created task: $it") }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun read(@PathVariable id: UUID) =
            taskService.read(id)
                    .also { logger?.info("Found task: $it") }

    @PutMapping("{id}")
    fun update(@PathVariable id: UUID, @RequestBody task: TaskService.InputTask) =
            taskService.update(id, task)
                    .also { logger?.info("Updated task from: $task to: $it ") }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: UUID) =
            taskService.delete(id)
                    .also { logger?.info("Deleted task id: $id") }
}