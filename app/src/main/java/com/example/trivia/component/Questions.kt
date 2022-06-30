package com.example.trivia.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trivia.model.Question
import com.example.trivia.screens.QuestionViewModel
import com.example.trivia.util.AppColor
import dagger.Provides
import java.lang.Exception

@Composable
fun Questions(viewModel: QuestionViewModel){

    val quesState=viewModel.listQues.collectAsState()
    val questionIndex= viewModel.questionIndex
    if (quesState.value.isLoading){
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }

    }else{
        val queList=quesState.value.data
        val questionSize=quesState.value.data?.size
        val questionItem=try {
            queList?.get(questionIndex.value)
        }catch (e:Exception){
             null
        }

        questionItem?.let {
            QuestionDisplay(question = it,questionIndex = questionIndex ,outOfQues = questionSize!!){
                          questionIndex.value=questionIndex.value+1
            }
        }

    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: Question,
    questionIndex: MutableState<Int>,
    outOfQues:Int=0,
    onNextClicked:(Int) -> Unit

){
    val choicesState= remember(question){
        question.choices.toMutableList()
    }

    val answerState= remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState= remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer:(Int) -> Unit= remember(question) {
        {
            answerState.value=it
            correctAnswerState.value=choicesState[it]==question.answer
        }
    }

    val pathEffect=PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)

    Surface(
        modifier = Modifier
        .fillMaxSize(),
        color = AppColor.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (questionIndex.value>=1){
                ShowProgress(questionIndex.value)
            }
            QuestionTracker(counter = questionIndex.value+1,outOf = outOfQues)
            DrawDotetLine(pathEffect = pathEffect)
            Spacer(modifier = Modifier.height(10.dp))
            
            Column {
                Text(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f)
                        .fillMaxWidth(),
                    text = "Question: ${question.question}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    color = AppColor.mOffWhite,
                )

                choicesState.forEachIndexed { index, anwser ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(AppColor.mLightGray, AppColor.mOffWhite)
                                ),
                                shape = RoundedCornerShape(corner = CornerSize(15.dp))
                            )
                            .clip(RoundedCornerShape(50))
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = (answerState.value==index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (correctAnswerState.value==true
                                    && answerState.value==index){
                                    Color.Green.copy(alpha = 0.2f)
                                }else{
                                    Color.Red.copy(alpha = 0.2f)
                                }
                            )
                        )
                        
                        Text(
                            text = anwser,
                            color = AppColor.mOffWhite,
                            modifier = Modifier.padding(start = 7.dp)
                        )

                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onNextClicked(questionIndex.value) },
                    modifier = Modifier
                        .padding(3.dp)
                        .fillMaxWidth(0.4f)
                        .height(45.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColor.mBlue
                    )
                ) {

                    Text(
                        text = "Next",
                        color = AppColor.mOffWhite,
                        modifier = Modifier.padding(5.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }

    }
}

@Composable
fun QuestionTracker(counter:Int=0,outOf:Int=0){

    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(
                style = SpanStyle(
                    color = AppColor.mLightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 27.sp
            )){
                append("Question $counter/")
                withStyle(style = SpanStyle(color = AppColor.mLightGray,
                fontSize = 15.sp,
                fontWeight = FontWeight.Light)){
                    append("$outOf")
                }
            }
        }
    },
    modifier = Modifier.padding(20.dp))

}



@Composable
fun DrawDotetLine(pathEffect: PathEffect){

    Canvas(modifier = Modifier
        .fillMaxWidth(),
        onDraw = {
        drawLine(color = AppColor.mLightGray,
        start = Offset(0f,0f),
        end = Offset(size.width,0f),
        pathEffect = pathEffect)
    })
}

@Preview
@Composable
fun ShowProgress(score:Int=0){

    val gradient= Brush.linearGradient(listOf(Color(0xfff95075), Color(0xffbe6be5)))

    val progressFactor= remember (score){
        mutableStateOf(score*0.005f)
    }

    Row(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(AppColor.mLightGray, AppColor.mLightPurple)
            ),
            shape = RoundedCornerShape(corner = CornerSize(34.dp))
        )
        .clip(RoundedCornerShape(50))
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {

        
        Button(
            onClick = {  },
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progressFactor.value)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )

        ) {


        }
    }

}
