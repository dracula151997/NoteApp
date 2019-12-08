package com.project.semicolon.noteapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.semicolon.noteapp.databinding.NoteListBinding;
import com.project.semicolon.noteapp.model.NoteResponse;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<NoteResponse> items;

    public NoteAdapter() {
        items = new ArrayList<>();
    }

    public void setItems(List<NoteResponse> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        NoteListBinding binding = NoteListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setNote(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private NoteListBinding binding;

        public ViewHolder(@NonNull NoteListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
