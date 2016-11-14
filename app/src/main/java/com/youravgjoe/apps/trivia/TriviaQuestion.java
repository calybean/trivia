package com.youravgjoe.apps.trivia;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by jcannon on 11/10/16.
 */

public class TriviaQuestion {

    private static final String TAG = "TriviaQuestion";

    private String category;
    private String difficulty;
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;
    private int numOfAnswers;

    private List<String> allAnswers;

    TriviaQuestion(String category, String difficulty, String question, String correctAnswer, List<String> incorrectAnswers) {
        this.category = category;
        this.difficulty = difficulty;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
        numOfAnswers = 1 + incorrectAnswers.size(); // 1 for correct answer, plus all incorrect answers
        allAnswers = incorrectAnswers;
        allAnswers.add(correctAnswer);
        shuffleAnswers();
    }

    TriviaQuestion() {
        // empty constructor
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = decode(category);
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = decode(difficulty);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = decode(question);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = decode(correctAnswer);
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        numOfAnswers = 1 + incorrectAnswers.size(); // 1 for correct answer, plus all incorrect answers

        this.incorrectAnswers = new ArrayList<>();
        for (int i = 0; i < incorrectAnswers.size(); i++) {
            this.incorrectAnswers.add(decode(incorrectAnswers.get(i)));
        }
    }

    public int getNumOfAnswers() {
        return numOfAnswers;
    }

    public List<String> getAllAnswers() {
        return allAnswers;
    }

    public void setAllAnswers() {
        if (correctAnswer.equals(null) || incorrectAnswers.isEmpty()) {
            Log.d(TAG, "Could not set all answers");
        }

        allAnswers = incorrectAnswers;
        allAnswers.add(correctAnswer);

        shuffleAnswers();
    }

    private String decode(String in) {
        Log.d(TAG, "in: " + in);
        try {
            byte[] bytes = Base64.decode(in, Base64.URL_SAFE);
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void shuffleAnswers() {
        long seed = System.nanoTime();
        Collections.shuffle(allAnswers, new Random(seed));
    }
}
