package org.x65nchanter.kandroid.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Column as AColumn

@Entity(name = "KColumn")
data class Column(
        @JsonProperty(required = true)
        @AColumn(length = 256, nullable = false)
        val name: String,

//        Положение фазы относительно других фаз
        @JsonProperty(required = true)
        @AColumn(name = "kColumnOrder", nullable = false)
        val order: Short,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "boardId")
        val board: Board? = null,

        @OneToMany(mappedBy = "column")
        val tasks: Collection<Task> = emptyList(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L
) {
//        Relationships
//        val tasks

//        Calc props
@Transient
val overflow = tasks.size
}