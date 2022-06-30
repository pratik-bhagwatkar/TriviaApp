package com.example.trivia.repository

import com.example.trivia.model.Question
import com.example.trivia.network.QuestionApi
import com.example.trivia.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {

      suspend fun getAllQuestion(): Flow<List<Question>> =flow{
           val response=api.getQuestions()
           emit(response)
      }
}