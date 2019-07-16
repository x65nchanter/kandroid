package org.x65nchanter.kandroid.services

import org.springframework.stereotype.Service
import org.x65nchanter.kandroid.data.Column
import org.x65nchanter.kandroid.data.ColumnRepository

@Service
class ColumnService(val columnRepository: ColumnRepository) {
    //TODO: CRUD Запросы к репозиторию досок
    fun getById(id: Long) = columnRepository.findById(id)

    fun getAll(): Iterable<Column> = columnRepository.findAll()

    fun add(board: Column) = columnRepository.save(board)

    fun edit(id: Long, board: Column) = columnRepository.save(board.copy(id = id))

    fun get(id: Long) = getById(id)

    fun remove(id: Long) = columnRepository.deleteById(id)
}