package com.codewiz_inc.selfe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button b1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button) findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Toast.makeText(getApplicationContext(), "CPU STARTED PLAYING! IT'S TIME TO BEAT IT UP", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, QuizFunActivity.class);
                startActivity(intent);

            }
        });

    }
    }

