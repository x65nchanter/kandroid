package org.x65nchanter.kandroid.data

import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Transient
import javax.persistence.Column as AColumn

@Entity(name = "KColumn")
class Column(
        @AColumn(length = 256)
        var name: String,

        @AColumn(name = "kColumnOrder")
        var order: Short,

        @ManyToOne(fetch = FetchType.EAGER)
        var board: Board? = null,

        @OneToMany(fetch = FetchType.EAGER, mappedBy = "column")
        var tasks: Collection<Task> = emptyList(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: UUID = UUID.randomUUID()
) {
        @Transient
        val overflow = tasks.size
}


@Transactional(propagation = Propagation.MANDATORY)
interface ColumnRepository : CrudRepository<Column, UUID>
