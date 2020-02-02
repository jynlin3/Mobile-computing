package com.example.keepcode;

import android.bluetooth.le.AdvertisingSetParameters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater inflater;

    Adapter(Context context){
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String title = "Sample Title";
        holder.nTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nTitle;
        public ViewHolder(@NonNull final View itemView){
            super(itemView);
            nTitle = itemView.findViewById(R.id.nTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), AddNote.class);
                    i.putExtra("title", "Sample Title");
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
