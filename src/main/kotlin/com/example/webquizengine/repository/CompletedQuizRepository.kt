package com.example.webquizengine.repository

import com.example.webquizengine.entity.CompletedQuizEntity
import com.example.webquizengine.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompletedQuizRepository : JpaRepository<CompletedQuizEntity, Long> {
    fun findAllByUserOrderByCompletedAtDesc(user: UserEntity, pageable: Pageable): Page<CompletedQuizEntity>?
    fun deleteAllById(id: Int)
}