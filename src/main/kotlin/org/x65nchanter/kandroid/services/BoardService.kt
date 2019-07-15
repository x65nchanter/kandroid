package org.x65nchanter.kandroid.services

import org.springframework.stereotype.Service
import org.x65nchanter.kandroid.data.Board
import org.x65nchanter.kandroid.data.BoardRepository

@Service
class BoardService(val boardRepository: BoardRepository) {
    //TODO: CRUD Запросы к репозиторию досок
    fun getById(id: Long) = boardRepository.findById(id)

    fun getAll(): Iterable<Board> = boardRepository.findAll()

    fun add(board: Board) = boardRepository.save(board)

    fun edit(id: Long, board: Board) = boardRepository.save(board.copy(id = id))

    fun get(id: Long) = getById(id)

    fun remove(id: Long) = boardRepository.deleteById(id)
}