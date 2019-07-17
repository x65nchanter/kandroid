package org.x65nchanter.kandroid.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import org.x65nchanter.kandroid.data.Column as KColumn

@Entity
data class Board(
        @JsonProperty(required = true)
        @Column(length = 256, nullable = false)
        val name: String,

        @Column(length = 2048, nullable = false)
        val description: String,

//        Временной интервал на выполнеие проекта
        val startAt: LocalDateTime?,
        val endAt: LocalDateTime?,

//        Статус новых досок по умалчанию Планируется
        val status: Short = BoardStatus.PLANING.statusCode,

        @OneToMany(mappedBy = "board")
        val columns: Collection<KColumn> = emptyList(),

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L
) {
    enum class BoardStatus(val statusCode: Short) {
        PLANING(0),
        ACTIVE(1),
        FINISHED(2)
    }
}