package com.example.webquizengine.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "completed_quizzes")
@SequenceGenerator(name = "completed_quizzes_generator", allocationSize = 1)
data class CompletedQuizEntity(

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "completed_quizzes_generator")
    val completedQuizId: Int? = null,

    @ManyToOne
    @JsonIgnore
    @JoinColumn(referencedColumnName = "id")
    var user: UserEntity? = null,

    var id: Int = 0,

    @Column(name = "completed_at")
    val completedAt: LocalDateTime = LocalDateTime.now()

)
