package edu.harvard.cs50.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout containerView;
        public TextView nameTextView;

        public NoteViewHolder(View view) {
            super(view);
            this.containerView = view.findViewById(R.id.note_row);
            this.nameTextView = view.findViewById(R.id.note_row_name);

            this.containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Note note = (Note) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), NoteActivity.class);
                    intent.putExtra("id", note.id);
                    intent.putExtra("title", note.title);
                    intent.putExtra("content", note.content);

                    context.startActivity(intent);
                }
            });
        }
    }

    public List<Note> notes = new ArrayList<>();

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note current = notes.get(position);

        holder.containerView.setTag(current);
        holder.nameTextView.setText(current.title);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void reload() {
        notes = MainActivity.database.noteDao().getAll();
        notifyDataSetChanged();
    }

    public void removeItem(int id, int position) {
        notes.remove(position);
        MainActivity.database.noteDao().delete(id);
        notifyItemRemoved(position);
    }

    public void restoreItem(Note note, int position) {
        notes.add(position, note);
        MainActivity.database.noteDao().save(note.id, note.title, note.content);
        notifyItemInserted(position);
    }
}