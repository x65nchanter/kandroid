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
import javax.persistence.ManyToOne
import org.x65nchanter.kandroid.data.Column as KColumn

@Entity
class Task(
        @Column(length = 256)
        var name: String,

        @Column(length = 2048)
        var description: String,

        var promotAt: LocalDateTime? = null,
        var endAt: LocalDateTime? = null,

        var priority: Short = 0,

        var complexity: Int?,

        @ManyToOne(fetch = FetchType.EAGER)
        var column: KColumn? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID = UUID.randomUUID()
) {
}

interface TaskRepository : CrudRepository<Task, UUID>