package com.youravgjoe.apps.trivia;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jcannon on 11/10/16.
 */

public class TriviaData {
    @SerializedName("category")
    public String category;

    @SerializedName("difficulty")
    public String difficulty;

    @SerializedName("question")
    public String question;

    @SerializedName("correct_answer")
    public String correctAnswer;

    @SerializedName("incorrect_answers")
    public List<String> incorrectAnswers;
}
