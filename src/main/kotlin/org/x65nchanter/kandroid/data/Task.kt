package org.x65nchanter.kandroid.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import org.x65nchanter.kandroid.data.Column as KColumn
@Entity
data class Task(
        @JsonProperty(required = true)
        @Column(length = 256, nullable = false)
        val name: String,

        @Column(length = 2048, nullable = false)
        val description: String,

//        Метка времени вхождения в текущую фазу и ожидаемая дата  оканчания задачи
        val promotAt: LocalDateTime?,
        val endAt: LocalDateTime?,

        @Column(name = "kTaskPriority", nullable = false)
        val priority: Short = 0,

        @Column(name = "kTaskComplexity")
        val complexity: Int?,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "columnId")
        val column: KColumn? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L
) {
//        Relationships
//        TODO:val worker ...?
}