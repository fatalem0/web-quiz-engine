package com.example.webquizengine.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity
@Table(name = "quizzes")
@SequenceGenerator(name = "quizzes_generator", allocationSize = 1)
data class QuizEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quizzes_generator")
    val id: Int? = null,

    @ManyToOne
    @JsonIgnore
    @JoinColumn(referencedColumnName = "id")
    val creator: UserEntity? = null,

    @Column
    val title: String,

    @Column
    val text: String,

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    val options: List<String>,

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    val answer: List<Int>?

)