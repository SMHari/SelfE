package com.codewiz_inc.selfe;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends Activity {

    CountDownTimer countDownTimer;
    TextView mTextField=null;
	EditText question = null;
	RadioButton answer1 = null;
	RadioButton answer2 = null;
	RadioButton answer3 = null;
	RadioButton answer4 = null;
	RadioGroup answers = null;
	Button finish = null;
	int selectedAnswer = -1;
	int quesIndex = 0;
	int numEvents = 0;
	int selected[] = null;
	int correctAns[] = null;
	boolean review =false;
	Button prev, next = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question);

        mTextField = (TextView) findViewById(R.id.mTextField);
        startTimer();

        TableLayout quizLayout = (TableLayout) findViewById(R.id.quizLayout);
		quizLayout.setVisibility(View.INVISIBLE);

		try {
			question = (EditText) findViewById(R.id.question);
			answer1 = (RadioButton) findViewById(R.id.a0);
			answer2 = (RadioButton) findViewById(R.id.a1);
			answer3 = (RadioButton) findViewById(R.id.a2);
			answer4 = (RadioButton) findViewById(R.id.a3);
			answers = (RadioGroup) findViewById(R.id.answers);
			RadioGroup questionLayout = (RadioGroup)findViewById(R.id.answers);
			Button finish = (Button)findViewById(R.id.finish);
			finish.setOnClickListener(finishListener);
			
			prev = (Button)findViewById(R.id.Prev);
			prev.setOnClickListener(prevListener);
			next = (Button)findViewById(R.id.Next);
			next.setOnClickListener(nextListener);

			
			selected = new int[QuizFunActivity.getQuesList().length()];
			Arrays.fill(selected, -1);
			correctAns = new int[QuizFunActivity.getQuesList().length()];
			Arrays.fill(correctAns, -1);


			this.showQuestion(0,review);

			quizLayout.setVisibility(View.VISIBLE);
			
		} catch (Exception e) {
			Log.e("", e.getMessage().toString(), e.getCause());
		} 

	}

    private void End() {
        setAnswer();
        //Calculate Score
        int score = 0;
        for(int i=0; i<correctAns.length; i++){
            if ((correctAns[i] != -1) && (correctAns[i] == selected[i]))
                score++;
        }
        AlertDialog alertDialog;
        alertDialog = new Builder(QuestionActivity.this).create();
        alertDialog.setTitle("Score");
        alertDialog.setMessage((score) +" out of " + (QuizFunActivity.getQuesList().length()));
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Retake", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                review = false;
                quesIndex=0;
                QuestionActivity.this.showQuestion(0, review);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Review", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                review = true;
                quesIndex=0;
                QuestionActivity.this.showQuestion(0, review);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Quit", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                review = false;
                finish();
            }
        });

        alertDialog.show();
    }


    private void showQuestion(int qIndex,boolean review) {
		try {
			JSONObject aQues = QuizFunActivity.getQuesList().getJSONObject(qIndex);
			String quesValue = aQues.getString("question");
			if (correctAns[qIndex] == -1) {
				String correctAnsStr = aQues.getString("answer");
				correctAns[qIndex] = Integer.parseInt(correctAnsStr);
			}
			
			question.setText(quesValue.toCharArray(), 0, quesValue.length());
			answers.check(-1);
			answer1.setTextColor(Color.BLACK);
			answer2.setTextColor(Color.BLACK);
			answer3.setTextColor(Color.BLACK);
			answer4.setTextColor(Color.BLACK);
			JSONArray ansList = aQues.getJSONArray("options");
			String aAns = ansList.getJSONObject(0).getString("opt");
			answer1.setText(aAns.toCharArray(), 0, aAns.length());
			aAns = ansList.getJSONObject(1).getString("opt");
			answer2.setText(aAns.toCharArray(), 0, aAns.length());
			aAns = ansList.getJSONObject(2).getString("opt");
			answer3.setText(aAns.toCharArray(), 0, aAns.length());
			aAns = ansList.getJSONObject(3).getString("opt");
			answer4.setText(aAns.toCharArray(), 0, aAns.length());
			Log.d("",selected[qIndex]+"");
			if (selected[qIndex] == 0)
				answers.check(R.id.a0);
			if (selected[qIndex] == 1)
				answers.check(R.id.a1);
			if (selected[qIndex] == 2)
				answers.check(R.id.a2);
			if (selected[qIndex] == 3)
				answers.check(R.id.a3);

			setScoreTitle();
			if (quesIndex == (QuizFunActivity.getQuesList().length()-1)) 
				next.setEnabled(false);
			
			if (quesIndex == 0)
				prev.setEnabled(false);
			
			if (quesIndex > 0)
				prev.setEnabled(true);
			
			if (quesIndex < (QuizFunActivity.getQuesList().length()-1))
				next.setEnabled(true);

			
			if (review) {
				Log.d("review",selected[qIndex]+""+correctAns[qIndex]);;	
				if (selected[qIndex] != correctAns[qIndex]) {
					if (selected[qIndex] == 0)
						answer1.setTextColor(Color.RED);
					if (selected[qIndex] == 1)
						answer2.setTextColor(Color.RED);
					if (selected[qIndex] == 2)
						answer3.setTextColor(Color.RED);
					if (selected[qIndex] == 3)
						answer4.setTextColor(Color.RED);
				}
				if (correctAns[qIndex] == 0)
					answer1.setTextColor(Color.GREEN);
				if (correctAns[qIndex] == 1)
					answer2.setTextColor(Color.GREEN);
				if (correctAns[qIndex] == 2)
					answer3.setTextColor(Color.GREEN);
				if (correctAns[qIndex] == 3)
					answer4.setTextColor(Color.GREEN);
			}
		} catch (Exception e) {
			Log.e(this.getClass().toString(), e.getMessage(), e.getCause());
		}
	}
	

	private OnClickListener finishListener;

    {
        finishListener = new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onClick(View v) {
                QuestionActivity.this.stopCountdown();
                setAnswer();
                //Calculate Score
                int score = 0;
                for (int i = 0; i < correctAns.length; i++) {
                    if ((correctAns[i] != -1) && (correctAns[i] == selected[i]))
                        score++;
                }
                AlertDialog alertDialog;
                alertDialog = new Builder(QuestionActivity.this).create();
                alertDialog.setTitle("Score");
                alertDialog.setMessage((score) + " out of " + (QuizFunActivity.getQuesList().length()));

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Retake", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        QuestionActivity.this. stopCountdown();
                        QuestionActivity.this.startTimer();
                        review = false;
                        quesIndex = 0;
                        QuestionActivity.this.showQuestion(0, review);

                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Review", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        review = true;
                        quesIndex = 0;
                        QuestionActivity.this.showQuestion(0, review);
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Quit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        review = false;
                        finish();
                    }
                });

                alertDialog.show();

            }
        };
    }

    private void setAnswer() {
		if (answer1.isChecked())
			selected[quesIndex] = 0;
		if (answer2.isChecked())
			selected[quesIndex] = 1;
		if (answer3.isChecked())
			selected[quesIndex] = 2;
		if (answer4.isChecked())
			selected[quesIndex] = 3;
		
		Log.d("",Arrays.toString(selected));
		Log.d("",Arrays.toString(correctAns));
		
	}
	
	private OnClickListener nextListener = new OnClickListener() {
		public void onClick(View v) {
			setAnswer();
			quesIndex++;
			if (quesIndex >= QuizFunActivity.getQuesList().length())
				quesIndex = QuizFunActivity.getQuesList().length() - 1;
			
			showQuestion(quesIndex,review);
		}
	};

	private OnClickListener prevListener = new OnClickListener() {
		public void onClick(View v) {
			setAnswer();
			quesIndex--;
			if (quesIndex < 0)
				quesIndex = 0;

			showQuestion(quesIndex,review);
		}
	};
	
	private void setScoreTitle() {
		this.setTitle("Self Evaluation" + "     " + (quesIndex+1)+ "/" + QuizFunActivity.getQuesList().length());
	}
    public void startTimer(){
        CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                mTextField.setText(hms);//set text
            }

            public void onFinish() {
                mTextField.setText("TIME'S UP!!");
                End();
            }
        }.start();
    }
    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}