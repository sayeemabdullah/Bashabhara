package com.example.bashabhara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBariwala , mBharatiya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBariwala = (Button) findViewById(R.id.bariwala);
        mBharatiya = (Button) findViewById(R.id.bharatiya);

        mBariwala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BariwalaLoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mBharatiya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BharatiyaLoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
