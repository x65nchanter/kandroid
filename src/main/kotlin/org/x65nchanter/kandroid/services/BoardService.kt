package org.x65nchanter.kandroid.services

import org.springframework.stereotype.Service
import org.x65nchanter.kandroid.data.Board
import org.x65nchanter.kandroid.data.BoardRepository
import java.time.LocalDateTime
import java.util.UUID

@Service
class BoardService(val boardRepository: BoardRepository) {
    data class OutputBoard(
            val id: UUID,
            val name: String,
            val description: String,
            val startAt: LocalDateTime?,
            val endAt: LocalDateTime?,
            val status: Board.BoardStatus
    )

    data class InputCreateBoard(
            val name: String,
            val description: String,
            val startAt: LocalDateTime?,
            val endAt: LocalDateTime?
    ) {
        fun toBoard() = Board(this.name, this.description, this.startAt, this.endAt)
    }

    data class InputBoard(val name: String, val description: String) {
        fun copyBoard(board: Board) = board.also {
            it.name = name
            it.description = description
        }
    }


    private fun getById(id: UUID) = boardRepository.findById(id)

    private fun getAll(): Iterable<Board> = boardRepository.findAll()

    private fun add(board: Board) = boardRepository.save(board)

    private fun deleteById(id: UUID) = boardRepository.deleteById(id)

    private fun Board.toOutputBoard() =
            OutputBoard(
                    this.id,
                    this.name,
                    this.description,
                    this.startAt,
                    this.endAt,
                    this.status
            )

    fun create(board: InputCreateBoard): OutputBoard {
        val entBrd = add(board.toBoard())
        return entBrd.toOutputBoard()
    }

    private fun findBoardByIdOrThrow(id: UUID) =
            boardRepository.findById(id).orElseThrow {
                throw Exception("Board not found: $id")
            }

    fun update(id: UUID, board: InputBoard): OutputBoard {
        val entBrd = findBoardByIdOrThrow(id)
        val entNew = board.copyBoard(entBrd)
        return add(entNew).toOutputBoard()
    }

    fun read(id: UUID): OutputBoard? = with(getById(id)) {
        var outBoard: OutputBoard? = null
        ifPresent { board ->
            outBoard = board.toOutputBoard()
        }
        outBoard
    }

    fun delete(id: UUID) = deleteById(id)

    fun index(): Iterable<OutputBoard> = getAll().map { it.toOutputBoard() }

    //TODO: column list
}