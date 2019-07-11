package org.x65nchanter.kandroid.services

import org.springframework.stereotype.Service
import org.x65nchanter.kandroid.data.Board
import org.x65nchanter.kandroid.data.BoardRepository

@Service
class BoardService(val boardRepository: BoardRepository) {
    //TODO: Запросы к репозиторию досок
    fun getAll(): Iterable<Board> = boardRepository.findAll()

    fun getById(id: Long) = boardRepository.findById(id)

    fun add(board: Board) = boardRepository.save(board)
}