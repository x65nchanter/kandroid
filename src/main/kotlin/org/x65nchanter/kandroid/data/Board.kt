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
data class Board(
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

//        Статус новых досок по умалчанию Планируется
        @JsonProperty
        @Column(nullable = false)
        val status: Short = BoardStatus.PLANING.statusCode,

        @Id
        @JsonProperty
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L
) {
    enum class BoardStatus(val statusCode: Short) {
        PLANING(0),
        ACTIVE(1),
        FINISHED(2)
    }
//        Relationships
//        val columns
//        val tasks
}