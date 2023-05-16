package com.example.webquizengine.repository

import com.example.webquizengine.entity.QuizEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuizRepository : JpaRepository<QuizEntity, Int> {
    fun findQuizById(id: Int): QuizEntity?
}