package com.example.trivia.screens

import com.example.trivia.model.Question

data class QuestionState(
    val data:List<Question>?=null,
    val error: String="",
    val isLoading:Boolean=false
)
