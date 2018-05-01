package com.dev.mevur.painter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dev.mevur.painter.view.CircleView;

public class MainActivity extends AppCompatActivity {
    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("main on create");
        circleView = findViewById(R.id.circle);
        circleView.setScore(98.9f);
        circleView.setCentralText("50fen");
        circleView.setStrokeWidth(30);
        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleView.load();
            }
        });
    }
}
