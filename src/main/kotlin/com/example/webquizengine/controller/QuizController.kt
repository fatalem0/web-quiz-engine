package com.example.webquizengine.controller

import com.example.webquizengine.dto.AnswerDTO
import com.example.webquizengine.dto.NewQuizDTO
import com.example.webquizengine.dto.ResponseDTO
import com.example.webquizengine.entity.CompletedQuizEntity
import com.example.webquizengine.entity.QuizEntity
import com.example.webquizengine.entity.UserEntity
import com.example.webquizengine.service.QuizService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/quizzes")
class QuizController(private val quizService: QuizService) {

    @PostMapping("/{id}/solve")
    fun solveQuiz(
        @RequestBody answer: AnswerDTO,
        @PathVariable id: Int,
        @AuthenticationPrincipal user: UserEntity
    ): ResponseEntity<ResponseDTO> =
        ResponseEntity.ok()
            .body(quizService.solveQuiz(id, answer, user) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND))

    @GetMapping("/{id}")
    fun getQuiz(@PathVariable id: Int): ResponseEntity<QuizEntity> =
        ResponseEntity.ok()
            .body(quizService.getQuiz(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND))

    @PostMapping
    fun createQuiz(
        @Valid @RequestBody newQuiz: NewQuizDTO,
        @AuthenticationPrincipal user: UserEntity
    ): ResponseEntity<QuizEntity> =
        ResponseEntity.ok().body(quizService.createQuiz(newQuiz, user))

    @GetMapping
    fun getAllQuizzes(@RequestParam(defaultValue = "0") page: Int): ResponseEntity<Page<QuizEntity>> =
        ResponseEntity.ok().body(quizService.getAllQuizzes(page))

    @DeleteMapping("/{id}")
    fun deleteQuiz(
        @PathVariable id: Int,
        @AuthenticationPrincipal user: UserEntity
    ): ResponseEntity<String> {
        val httpStatus: HttpStatus = quizService.deleteQuiz(id, user)
        val resString: String = when (httpStatus.name) {
            "NOT_FOUND"  -> "Quiz not found"
            "NO_CONTENT" -> "Operation was successfully completed"
            "FORBIDDEN"  -> "The specified user is not the author of this quiz"
            else         -> ""
        }

        return ResponseEntity(resString, httpStatus)
    }

    @GetMapping("/completed")
    fun getAllCompletionsByUser(
        @RequestParam(defaultValue = "0") page: Int,
        @AuthenticationPrincipal user: UserEntity
    ): ResponseEntity<Page<CompletedQuizEntity>> =
        ResponseEntity.ok()
            .body(
                quizService
                    .getAllCompletionsByUser(page, user) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
            )

}