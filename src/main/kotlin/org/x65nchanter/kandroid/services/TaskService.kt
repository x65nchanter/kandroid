package org.x65nchanter.kandroid.services

import org.springframework.stereotype.Service
import org.x65nchanter.kandroid.data.Task
import org.x65nchanter.kandroid.data.TaskRepository

@Service
class TaskService(val taskRepository: TaskRepository) {
    fun getById(id: Long) = taskRepository.findById(id)

    fun getAll(): Iterable<Task> = taskRepository.findAll()

    fun add(board: Task) = taskRepository.save(board)

    fun edit(id: Long, board: Task) = taskRepository.save(board.copy(id = id))

    fun get(id: Long) = getById(id)

    fun remove(id: Long) = taskRepository.deleteById(id)
}