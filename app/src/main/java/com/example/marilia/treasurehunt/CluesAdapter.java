package com.example.marilia.treasurehunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marilia.treasurehunt.database.Clue;
import com.example.marilia.treasurehunt.database.TreasureHunt;

public class CluesAdapter extends RecyclerView.Adapter<CluesAdapter.ViewHolder> {
    private Clue[] clues;
    private TreasureHunt[] ths;
    private Context context;

    public CluesAdapter(Clue[] clues) { this.clues = clues; }

    @Override
    public CluesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_clues,
                parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder!=null && clues[position]!=null) {
            holder.clue.setText(clues[position].clue);
            holder.description.setText(clues[position].description);
        }
    }

    @Override
    public int getItemCount() {
        return clues.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clue;
        TextView description;

        public ViewHolder(View textView){
            super(textView);
            clue = (TextView) textView.findViewById(R.id.clue);
            description = (TextView) textView.findViewById(R.id.description);
        }

    }

}

