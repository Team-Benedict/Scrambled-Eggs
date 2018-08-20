package forkbomb.scrambledeggs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {
    //controls what questions will be used for the QuizActivity
    private QuestionHandler questionHandler;
    //controls what question the user is on
    private int questionNumber = 0;

    //view elements
    //changes the text for the question
    TextView question;
    ListView answers;
    Button button;

    //adapter for filling listview
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //used for handling questions, answers, and user selections
        questionHandler = new QuestionHandler();

        //sets up activity elements
        question = (TextView) findViewById(R.id.tv_question);
        answers = (ListView) findViewById(R.id.answers);
        button = (Button) findViewById(R.id.button);

        //event handling for when item in list view is clicked
        answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //checks if question is not selected
                if (!questionHandler.quizQuestions[questionNumber].userAnswers.contains(answers.getItemAtPosition(position).toString())) {
                    //switches color, adds to array
                    view.setBackgroundColor(Color.parseColor("#CCCCCC"));
                    questionHandler.quizQuestions[questionNumber].userAnswers.add(answers.getItemAtPosition(position).toString());
                }
                //checks if question is selected
                else {
                    //switches color, removes from array
                    view.setBackgroundColor(Color.TRANSPARENT);
                    questionHandler.quizQuestions[questionNumber].userAnswers.remove(answers.getItemAtPosition(position).toString());
                }

                //changes text on button depending on if anything is selected
                button.setText((questionHandler.quizQuestions[questionNumber].userAnswers.size() > 0) ? "NEXT" : "SKIP");
            }
        });

        //generates starting possible answers
        for (int i = 0; i < 4; i++)
            questionHandler.quizQuestions[questionNumber].displayedAnswers[i] = questionHandler.generateAnswer(questionNumber);

        handleQuiz();
    }

    //controls QuizActivity
    public void handleQuiz(){
        question.setText(questionHandler.quizQuestions[questionNumber].question);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questionHandler.quizQuestions[questionNumber].displayedAnswers);
        answers.setAdapter(adapter);
    }

    //handles button press
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button:
                //resets displayed answers array
                questionHandler.quizQuestions[questionNumber].displayedAnswers = new String[4];
                //increments question number
                if (questionNumber < getIntent().getIntExtra("length",2) - 1)
                    questionNumber++;
                else questionNumber = 0;
                //generates new answers
                for (int i = 0; i < 4; i++)
                    questionHandler.quizQuestions[questionNumber].displayedAnswers[i] = questionHandler.generateAnswer(questionNumber);

                //resets text
                button.setText("SKIP");
                handleQuiz();
                break;
            default:
                break;
        }
    }

    //navbar back button press
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //back button press event
    @Override
    public void onBackPressed() {
        //kills the activity
        finish();
    }

}