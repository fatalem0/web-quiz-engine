package com.example.webquizengine.service

import com.example.webquizengine.dto.AnswerDTO
import com.example.webquizengine.dto.NewQuizDTO
import com.example.webquizengine.dto.ResponseDTO
import com.example.webquizengine.entity.CompletedQuizEntity
import com.example.webquizengine.entity.QuizEntity
import com.example.webquizengine.entity.UserEntity
import com.example.webquizengine.repository.CompletedQuizRepository
import com.example.webquizengine.repository.QuizRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QuizService(
    private val quizRepository: QuizRepository,
    private val completedQuizRepository: CompletedQuizRepository
) {

    private val rightAnswerResponse: ResponseDTO =
        ResponseDTO(true, "Congratulations, you're right!")

    private val wrongAnswerResponse: ResponseDTO =
        ResponseDTO(false, "Wrong answer! Please, try again.")

    fun solveQuiz(id: Int, answer: AnswerDTO, user: UserEntity): ResponseDTO? {
        val quiz: QuizEntity? = getQuiz(id)

        return if (quiz == null) null else {
            if (answer.answer == quiz.answer) {
                completedQuizRepository.save(
                    CompletedQuizEntity(
                        user = user,
                        id = id
                    )
                )

                rightAnswerResponse
            } else wrongAnswerResponse
        }
    }

    fun getQuiz(id: Int): QuizEntity? = quizRepository.findQuizById(id)

    fun createQuiz(newQuiz: NewQuizDTO, user: UserEntity): QuizEntity {
        val quiz = QuizEntity(
            creator = user,
            title = newQuiz.title,
            text = newQuiz.text,
            options = newQuiz.options,
            answer = newQuiz.answer ?: listOf()
        )

        quizRepository.save(quiz)

        return quiz
    }

    fun getAllQuizzes(page: Int): Page<QuizEntity> =
        quizRepository.findAll(PageRequest.of(page, 10))

    @Transactional
    fun deleteQuiz(id: Int, user: UserEntity): HttpStatus {
        val quiz: QuizEntity = getQuiz(id) ?: return HttpStatus.NOT_FOUND

        return if (quiz.creator?.id == user.id) {
            quizRepository.delete(quiz)
            completedQuizRepository.deleteAllById(id)

            HttpStatus.NO_CONTENT
        } else {
            HttpStatus.FORBIDDEN
        }
    }

    fun getAllCompletionsByUser(page: Int, user: UserEntity): Page<CompletedQuizEntity>? =
        completedQuizRepository.findAllByUserOrderByCompletedAtDesc(user, PageRequest.of(page, 10))

}