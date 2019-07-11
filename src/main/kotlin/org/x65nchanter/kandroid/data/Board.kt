package org.x65nchanter.kandroid.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

@Entity
data class Board(

//         Columns
        @JsonProperty(required = true)
        @Column(length = 256, nullable = false)
        val name: String,

        @JsonProperty
        @Column(length = 2048, nullable = false)
        val description: String,

//        Временной интервал на выполнеие проекта
        @JsonProperty
        @Temporal(TemporalType.TIMESTAMP)
        val startAt: Date?,
        @JsonProperty
        @Temporal(TemporalType.TIMESTAMP)
        val endAt: Date?,

        @Id
        @JsonProperty
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L
) {
//        Relationships
//        val columns
//        val tasks
}