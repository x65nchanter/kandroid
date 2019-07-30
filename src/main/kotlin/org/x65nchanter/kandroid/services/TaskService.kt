package org.x65nchanter.kandroid.services

import org.springframework.stereotype.Service
import org.x65nchanter.kandroid.data.Column
import org.x65nchanter.kandroid.data.ColumnRepository
import org.x65nchanter.kandroid.data.Task
import org.x65nchanter.kandroid.data.TaskRepository
import java.time.LocalDateTime
import java.util.UUID

@Service
class TaskService(val taskRepository: TaskRepository, val columnRepository: ColumnRepository) {
    data class OutputTask(
            val id: UUID,
            val name: String,
            val description: String,
            val promotAt: LocalDateTime?,
            val endAt: LocalDateTime?,
            val priority: Short,
            val complexity: Int?
    )

    data class InputTask(val name: String, val description: String, val priority: Short) {
        fun copyTask(task: Task) = task.also {
            it.name = name
            it.description = description
            it.priority = priority
        }
    }

    data class InputCreateTask(val name: String, val description: String, val priority: Short, val complexity: Int?) {
        fun toTask() = Task(this.name, this.description, priority = this.priority, complexity = this.complexity)
    }

    private fun getById(id: UUID) = taskRepository.findById(id)

    private fun getAll(): Iterable<Task> = taskRepository.findAll()

    private fun add(task: Task) = taskRepository.save(task)

    private fun deleteById(id: UUID) = taskRepository.deleteById(id)

    private fun Column.attach(task: Task) =
            columnRepository.save(
                    this.apply { tasks += task } // Effect: expand list of tasks
            )

    private fun findColumnByIdOrElseThrow(columnId: UUID) =
            columnRepository.findById(columnId).orElseThrow {
                throw Exception("Column not found: $columnId")
            }

    private fun Task.toOutputTask() =
            OutputTask(
                    this.id,
                    this.name,
                    this.description,
                    this.promotAt,
                    this.endAt,
                    this.priority,
                    this.complexity
            )

    fun create(task: InputCreateTask, columnId: UUID): OutputTask {
        val entTask = add(task.toTask())
        findColumnByIdOrElseThrow(columnId).attach(entTask)
        return entTask.toOutputTask()
    }

    private fun findTaskByIdOrElseThrow(id: UUID) =
            taskRepository.findById(id).orElseThrow {
                throw Exception("Task not found: $id")
            }

    fun update(id: UUID, task: InputTask): OutputTask {
        val entTsk = findTaskByIdOrElseThrow(id)
        val newTsk = task.copyTask(entTsk)
        return add(newTsk).toOutputTask()
    }

    fun read(id: UUID): OutputTask? = with(getById(id)) {
        var outTask: OutputTask? = null
        ifPresent { task ->
            outTask = task.toOutputTask()
        }
        outTask
    }

    fun delete(id: UUID) = deleteById(id)

    fun index(): Iterable<OutputTask> = getAll().map { it.toOutputTask() }

}