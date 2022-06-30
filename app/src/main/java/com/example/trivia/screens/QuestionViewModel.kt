package com.example.trivia.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trivia.model.Question
import com.example.trivia.repository.QuestionRepository
import com.example.trivia.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val repository: QuestionRepository):ViewModel() {

    private val _listQue= MutableStateFlow<QuestionState>(QuestionState())
    val listQues: StateFlow<QuestionState> =_listQue
    val questionIndex= mutableStateOf(0)


    init {
        getAllQuestions()
    }

    fun getAllQuestions() {
        _listQue.value = QuestionState(isLoading = true)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getAllQuestion().collect {
                    _listQue.value = QuestionState(data = it)

                }
            }
        }catch (e:Exception){
            _listQue.value = QuestionState(error ="Something went wrong please try again")
        }

    }

}

