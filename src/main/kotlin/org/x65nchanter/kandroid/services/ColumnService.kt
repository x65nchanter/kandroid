package org.x65nchanter.kandroid.services

import org.springframework.stereotype.Service
import org.x65nchanter.kandroid.data.Board
import org.x65nchanter.kandroid.data.BoardRepository
import org.x65nchanter.kandroid.data.Column
import org.x65nchanter.kandroid.data.ColumnRepository
import java.util.UUID



@Service
class ColumnService(val columnRepository: ColumnRepository, val boardRepository: BoardRepository) {

    data class OutputColumn(val id: UUID, val name: String, val order: Short, val overflow: Int)

    data class InputColumn(val name: String, val order: Short) {
        fun toColumn() = Column(this.name, this.order)
        fun copyColumn(column: Column) = column.also {
            it.name = name
            it.order = order
        }
    }

    private fun getById(id: UUID) = columnRepository.findById(id)

    private fun deleteById(id: UUID) = columnRepository.deleteById(id)

    private fun getAll(): Iterable<Column> = columnRepository.findAll()

    private fun add(column: Column) = columnRepository.save(column)

    private fun Board.attach(column: Column) =
            boardRepository.save(this.apply { columns += column }) // Effect: expand list of columns

    private fun findBoardByIdOrElseThrow(boardId: UUID) =
            boardRepository.findById(boardId).orElseThrow {
                //FIXME:
                throw Exception("Board not found: $boardId")
            }

    private fun Column.toOutputColumn() = OutputColumn(this.id, this.name, this.order, this.overflow)

    fun create(column: InputColumn, boardId: UUID): OutputColumn {
        val entCol = add(column.toColumn())
        findBoardByIdOrElseThrow(boardId).attach(entCol)
        return entCol.toOutputColumn()
    }

    private fun findColumnByIdOrElseThrow(columnId: UUID) =
            getById(columnId).orElseThrow {
                throw Exception("Column not found: $columnId")
            }

    fun update(id: UUID, column: InputColumn): OutputColumn {
        val entCol = findColumnByIdOrElseThrow(id)
        val newCol = column.copyColumn(entCol)
        return add(newCol).toOutputColumn()
    }

    fun read(id: UUID) = with(getById(id)) {
        var outColumn: OutputColumn? = null
        ifPresent { column ->
            outColumn = column.toOutputColumn()
        }
        outColumn
    }

    fun delete(id: UUID) = deleteById(id)

    fun index(): Iterable<OutputColumn> = getAll().map { column -> column.toOutputColumn() }

    //TODO: task list
}