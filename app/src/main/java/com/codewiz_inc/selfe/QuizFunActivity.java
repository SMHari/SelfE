package com.codewiz_inc.selfe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class QuizFunActivity extends Activity{
    Intent menu = null;
	BufferedReader bReader = null;
	static JSONArray quesList = null;
    Object valObj =new Object();
    JSONArray ques = new JSONArray();
    //static JSONObject quesList = null;



    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Thread thread = new Thread() {
        	public void run() {
		        try {
		        	Thread.sleep(1*1000);
			        finish();
			        loadQuestions();
			        Intent intent = new Intent(QuizFunActivity.this, QuestionActivity.class);
			        QuizFunActivity.this.startActivity(intent);
	        	} catch (Exception e) {
	        	} 
        	}
        };
        thread.start();

     }
    
    private void loadQuestions() throws Exception {
    	try {
		InputStream questions = this.getBaseContext().getResources().openRawResource(R.raw.questions);
		bReader = new BufferedReader(new InputStreamReader(questions));
		StringBuilder quesString = new StringBuilder();
		String aJsonLine = null;
		while ((aJsonLine = bReader.readLine()) != null) {
			quesString.append(aJsonLine);
		}
		Log.d(this.getClass().toString(), quesString.toString());
		JSONObject quesObj = new JSONObject(quesString.toString());
            Log.d(this.getClass().getName(), "quesjson  " + quesObj.length());
            Log.d(this.getClass().getName(), "ques  " + quesObj);

            for (Iterator<String> it = quesObj.keys(); it.hasNext(); ) {
                Object keyObj = it.next();
                String key = (String)keyObj;
                 valObj = quesObj.get(key);
                Log.d(this.getClass().getName(), "indjson  " + valObj);
                //quesList.put(valObj);
                quesList=ques.put(valObj);
            }

            Log.d(this.getClass().getName(), "queslist  " + quesList);
		//quesList = quesObj.getJSONArray("");
            /*for(int i=0;i<quesObj.length();i++) {
                quesList=ques.put(valObj);
            }*/
		Log.d(this.getClass().getName(), "Num Questions " + quesList);
        } catch (Exception e){
    		
    	} finally {
			try {
				bReader.close();
			} catch (Exception e) {
				Log.e("", e.getMessage().toString(), e.getCause());
			}

    	}
    	

    }
    
    public static JSONArray getQuesList() {
    	return quesList;
    }

}

