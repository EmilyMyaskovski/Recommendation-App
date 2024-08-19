package com.example.recom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BoardActivity extends AppCompatActivity {

    private EditText board_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        findViews();
        initViews();
    }

    private void initViews() {
        Button publishButton = findViewById(R.id.publish);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String boardName = board_name.getText().toString();

                if (!boardName.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("BOARD_NAME", boardName);
                    setResult(RESULT_OK, resultIntent);

                    Log.d("BoardActivity", "Returning board name: " + boardName);
                    finish();
                } else {
                    board_name.setError("Please enter a board name");
                }
            }
        });
    }

    private void findViews() {
        board_name = findViewById(R.id.board_name);
    }
}
