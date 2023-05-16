package com.example.webquizengine.controller

import com.example.webquizengine.config.BCryptConfig
import com.example.webquizengine.dto.UserDTO
import com.example.webquizengine.entity.UserEntity
import com.example.webquizengine.repository.UserRepository
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/register")
class UserController(
    private val userRepository: UserRepository,
    private val bCryptConfig: BCryptConfig
) {

    @PostMapping
    fun createUser(@Valid @RequestBody newUser: UserDTO): ResponseEntity<String> {
        val user: UserEntity? = userRepository.findUserByEmail(newUser.email)

        return if (user == null) {
            val newUserEntity = UserEntity(
                email = newUser.email,
                password = bCryptConfig.bCryptPasswordEncoder().encode(newUser.password)
            )

            userRepository.save(newUserEntity)

            ResponseEntity.ok().body(
                "User registered successfully with email=${newUserEntity.email} and password=${newUserEntity.password}"
            )
        } else {
            ResponseEntity("User already exists", HttpStatus.BAD_REQUEST)
        }
    }

}