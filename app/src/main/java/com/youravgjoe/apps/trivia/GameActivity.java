package com.youravgjoe.apps.trivia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    private static final String DIFFICULTY = "DIFFICULTY";
    private static final String NUMBER_OF_QUESTIONS = "NUMBER_OF_QUESTIONS";

    private List<TriviaQuestion> mQuestionList;
    private List<String> mAnswerList;

    private TextView mScoreTextView;
    private TextView mQuestionTextView;
    private RadioGroup mRadioGroup;


    private int mCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mQuestionList = new ArrayList<>();
        mAnswerList = new ArrayList<>();

        setupViews();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            callApi(extras.getString(NUMBER_OF_QUESTIONS), extras.getString(DIFFICULTY).toLowerCase());
        } else {
            Log.d(TAG, "Error getting difficulty and number of questions.");
            finish();
        }
    }

    private void setupViews() {
        mScoreTextView = (TextView) findViewById(R.id.score);
        mScoreTextView.setText(mCorrect + "/" + mQuestionList.size());
        mQuestionTextView = (TextView) findViewById(R.id.question);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
    }

    private void startGame() {
        mQuestionTextView.setText(mQuestionList.get(0).getQuestion());

        mAnswerList = mQuestionList.get(0).getAllAnswers();

        for (int i = 0; i < mQuestionList.get(0).getNumOfAnswers(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(mAnswerList.get(i));
            radioButton.setTextSize(18);
            mRadioGroup.addView(radioButton);
        }
    }

    private void callApi(String numOfQuestions, String difficulty) {
        final String BASE_URL = "https://www.opentdb.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApiEndpointInterface apiService = retrofit.create(MyApiEndpointInterface.class);

        Call<ResponseBody> result = apiService.getTriviaQuestions(numOfQuestions, difficulty);

        Log.d(TAG, result.toString());

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject responseJson;
                JSONArray questionArray;
                try {
                    responseJson = new JSONObject(response.body().string());
                    questionArray = responseJson.getJSONArray("results");

                    for (int i = 0; i < questionArray.length(); i++) {
                        JSONObject questionJson = questionArray.getJSONObject(i);
                        TriviaQuestion question = new TriviaQuestion();
                        question.setCategory(questionJson.getString("category"));
                        question.setDifficulty(questionJson.getString("difficulty"));
                        question.setQuestion(questionJson.getString("question"));
                        question.setCorrectAnswer(questionJson.getString("correct_answer"));
                        JSONArray incorrectAnswers = questionJson.getJSONArray("incorrect_answers");
                        List<String> incorrectAnswersList = new ArrayList<>();
                        for (int j = 0; j < incorrectAnswers.length(); j++) {
                            incorrectAnswersList.add(incorrectAnswers.getString(j));
                        }
                        question.setIncorrectAnswers(incorrectAnswersList);

                        List<String> allAnswers = incorrectAnswersList;
                        allAnswers.add(question.getCorrectAnswer());
                        question.setAllAnswers(allAnswers);

                        mQuestionList.add(question);
                    }

                    startGame();
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d(TAG, "IOException");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("api.php")
        Call<ResponseBody> getTriviaQuestions(@Query("amount") String amount, @Query("difficulty") String difficulty);
    }
}
