package com.youravgjoe.apps.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String DIFFICULTY = "DIFFICULTY";
    private static final String NUMBER_OF_QUESTIONS = "NUMBER_OF_QUESTIONS";

    private String mDifficulty;
    private String mNumOfQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // start these off as default:
        mDifficulty = "Easy";
        mNumOfQuestions = "10";

        // difficulty spinner
        final List<String> difficulties = new ArrayList<>();
        difficulties.add("Easy");
        difficulties.add("Medium");
        difficulties.add("Hard");
        final Spinner difficultySpinner = (Spinner) findViewById(R.id.spinner_difficulty);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, difficulties);
        difficultySpinner.setAdapter(difficultyAdapter);

        // difficulty spinner
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDifficulty = difficulties.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "No difficulty selected");
            }
        });

        // number of questions spinner
        final List<String> numOfQuestions = new ArrayList<>();
        numOfQuestions.add("10");
        numOfQuestions.add("20");
        numOfQuestions.add("30");
        Spinner numOfQuestionsSpinner = (Spinner) findViewById(R.id.spinner_number_of_questions);
        ArrayAdapter<String> numOfQuestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numOfQuestions);
        numOfQuestionsSpinner.setAdapter(numOfQuestionsAdapter);

        numOfQuestionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mNumOfQuestions = numOfQuestions.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "No num of questons selected");
            }
        });

        final Button startGame = (Button) findViewById(R.id.start_game);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGameIntent = new Intent(MainActivity.this, GameActivity.class);
                startGameIntent.putExtra(DIFFICULTY, mDifficulty);
                startGameIntent.putExtra(NUMBER_OF_QUESTIONS, mNumOfQuestions);
                startActivity(startGameIntent);
            }
        });

    }
}
