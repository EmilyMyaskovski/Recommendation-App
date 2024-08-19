package com.example.recom.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.recom.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recom.Models.Board;

import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private ArrayList<Board> boardList;

    public BoardAdapter(ArrayList<Board> boardList) {
        this.boardList = boardList;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_board_skeleton, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Board board = boardList.get(position);
        holder.boardNameTextView.setText(board.getName());
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {

        public TextView boardNameTextView;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            boardNameTextView = itemView.findViewById(R.id.board_name);
        }
    }
}

