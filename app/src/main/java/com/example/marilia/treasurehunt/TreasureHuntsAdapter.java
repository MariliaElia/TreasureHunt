package com.example.marilia.treasurehunt;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.marilia.treasurehunt.database.Clue;
import com.example.marilia.treasurehunt.database.TreasureHunt;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TreasureHuntsAdapter extends RecyclerView.Adapter<TreasureHuntsAdapter.ViewHolder> {
    private TreasureHunt[] ths;
    private Context context;
    private ItemClickListener itemClickListener;

    public TreasureHuntsAdapter(TreasureHunt[] ths) { this.ths = ths; }

    public TreasureHuntsAdapter(TreasureHunt[] ths, ItemClickListener itemClickListener) {
        this.ths = ths;
        this.itemClickListener = itemClickListener;
    }

    public TreasureHuntsAdapter(Context cont, TreasureHunt[] th) {
        super();
        this.ths = th;
        context = cont;
    }

    @Override
    public TreasureHuntsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ths,
                parent, false);
        ViewHolder holder = new ViewHolder(v, itemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder!=null && ths[position]!=null) {
            holder.title.setText(ths[position].title);
            holder.description.setText(ths[position].description);
            holder.start.setText("Starts: " + dateToString(ths[position].open_on));
            holder.end.setText("Ends: " + dateToString(ths[position].close_on));
            holder.place.setText(ths[position].town + "," + ths[position].country);

        }
    }

    @Override
    public int getItemCount() {
        return ths.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        TextView start;
        TextView end;
        TextView place;

        private ItemClickListener itemClickListener;


        public ViewHolder(View textView, ItemClickListener itemClickListener){
            super(textView);
            title = (TextView) textView.findViewById(R.id.title);
            description = (TextView) textView.findViewById(R.id.description);
            start = (TextView) textView.findViewById(R.id.start);
            end = (TextView) textView.findViewById(R.id.end);
            place = (TextView) textView.findViewById(R.id.place);
            this.itemClickListener = itemClickListener;

            //attached an OnClickListener to the textView
            textView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v){
            //when the item is clicked the ItemClickListener interface calls the nClick method
            //passing the adapter position
            itemClickListener.onClick(v, getAdapterPosition());
        }

    }


    private String dateToString(Date date){
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = dateFormat.format(date);
            return strDate;
        } else {
            return "";
        }
    }

}

