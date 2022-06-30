package com.example.trivia.network

import com.example.trivia.model.Question
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface QuestionApi {
    @GET("world.json")
    suspend fun getQuestions(): List<Question>

}