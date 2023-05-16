package com.example.webquizengine.repository

import com.example.webquizengine.entity.UserEntity
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserEntity, Int> {
    fun findUserByEmail(email: String?): UserEntity?
}