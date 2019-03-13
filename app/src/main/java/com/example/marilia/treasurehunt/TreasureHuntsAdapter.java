package com.example.marilia.treasurehunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marilia.treasurehunt.database.Clue;
import com.example.marilia.treasurehunt.database.TreasureHunt;

import java.util.Date;

public class TreasureHuntsAdapter extends RecyclerView.Adapter<TreasureHuntsAdapter.ViewHolder> {
    private TreasureHunt[] ths;
    private Context context;

    public TreasureHuntsAdapter(TreasureHunt[] ths) {this.ths = ths; }

    public TreasureHuntsAdapter(Context cont, TreasureHunt[] th) {
        super();
        this.ths = th;
        context = cont;
    }

    @Override
    public TreasureHuntsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ths,
                parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder!=null && ths[position]!=null) {
            holder.title.setText(ths[position].title);
            holder.start.setText(dateToString(ths[position].open_on));
            holder.end.setText(dateToString(ths[position].close_on));
            holder.place.setText(ths[position].town + "," + ths[position].country);
        }
    }

    @Override
    public int getItemCount() {
        return ths.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView start;
        TextView end;
        TextView place;

        public ViewHolder(View textView){
            super(textView);
            title = (TextView) textView.findViewById(R.id.title);
            start = (TextView) textView.findViewById(R.id.start);
            end = (TextView) textView.findViewById(R.id.end);
            place = (TextView) textView.findViewById(R.id.place);
        }

    }

    public String dateToString(Date date){
        return "";
    }

}

