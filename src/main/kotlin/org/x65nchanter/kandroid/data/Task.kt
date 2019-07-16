package org.x65nchanter.kandroid.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
data class Task(
        @JsonProperty(required = true)
        @Column(length = 256, nullable = false)
        val name: String,

        @JsonProperty
        @Column(length = 2048, nullable = false)
        val description: String,

//        Метка времени вхождения в текущую фазу и ожидаемая дата  оканчания задачи
        @JsonProperty
        @Temporal(TemporalType.TIMESTAMP)
        val promotAt: Date?,
        @JsonProperty
        @Temporal(TemporalType.TIMESTAMP)
        val endAt: Date?,

        @JsonProperty
        @Column(name = "kTaskPriority", nullable = false)
        val priority: Short = 0,

        @JsonProperty
        @Column(name = "kTaskComplexity")
        val complexity: Int?,

        @Id
        @JsonProperty
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L
) {
//        Relationships
//        val column
//        val worker
}