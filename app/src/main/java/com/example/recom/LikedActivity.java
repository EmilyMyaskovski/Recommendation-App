package com.example.recom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recom.Adapter.BoardAdapter;
import com.example.recom.Models.Board;

import java.util.ArrayList;

public class LikedActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_NEW_BOARD = 1;

    private ImageView profile_button;
    private ImageView create_button;
    private ImageView search_button;
    private ImageView home_button;
    private TextView liked_title;
    private ImageButton back_button;
    private ImageButton plus_button;
    private RecyclerView recycler_view;
    private BoardAdapter adapter;
    private ArrayList<Board> boardList;
    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liked_activity);

        findViews();
        initViews();

        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        boardList = new ArrayList<>();
        adapter = new BoardAdapter(boardList);
        recycler_view.setAdapter(adapter);
    }

    protected void updateSubtitle() {
        subtitle.setText(boardList.size() + " projects");
        Log.d("LikedActivity", "Subtitle updated to: " + boardList.size() + " projects");
    }

    private void initViews() {
        profile_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikedActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        create_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikedActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        search_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikedActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        home_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        plus_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikedActivity.this, BoardActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_BOARD);
            }
        });
    }

    private void findViews() {
        profile_button = findViewById(R.id.profile_button);
        search_button = findViewById(R.id.search_button);
        create_button = findViewById(R.id.create_button);
        home_button = findViewById(R.id.home_button);
        liked_title = findViewById(R.id.liked_title);
        back_button = findViewById(R.id.back_button);
        plus_button = findViewById(R.id.plus_button);
        recycler_view = findViewById(R.id.recycler_view);
        subtitle = findViewById(R.id.subtitle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NEW_BOARD && resultCode == RESULT_OK && data != null) {
            String boardName = data.getStringExtra("BOARD_NAME");
            if (boardName != null && !boardName.isEmpty()) {
                boardList.add(new Board(boardName));
                adapter.notifyItemInserted(boardList.size() + 1); // Notify adapter about the new item
                updateSubtitle(); // Update subtitle after adding the new board

                Log.d("LikedActivity", "New board added: " + boardName);
            }
        } else {
            Log.d("LikedActivity", "onActivityResult not triggered as expected");
        }
    }
}
