package org.x65nchanter.kandroid.data

import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import org.x65nchanter.kandroid.data.Column as KColumn

@Entity
class Board(
        @Column(length = 256)
        var name: String,

        @Column(length = 2048)
        var description: String,

        var startAt: LocalDateTime? = null,
        var endAt: LocalDateTime? = null,

        @Column(columnDefinition = "smallint")
        var status: BoardStatus = BoardStatus.PLANING,

        @OneToMany(fetch = FetchType.EAGER, mappedBy = "board")
        var columns: Collection<KColumn> = emptyList(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID = UUID.randomUUID()
) {
    enum class BoardStatus {
        PLANING,
        ACTIVE,
        FINISHED
    }

}

interface BoardRepository : CrudRepository<Board, UUID>