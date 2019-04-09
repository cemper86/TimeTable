package ru.stairenx.nvsutimetable.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.item.PairItem;

public class PairAdapter  extends RecyclerView.Adapter<PairAdapter.PairViewHolder> {

    private List<PairItem> data;

    public PairAdapter(List<PairItem> data) {
        this.data = data;
    }

    static class PairViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView group;
        TextView pair;
        TextView discipline;
        TextView vid;
        TextView aud;
        TextView teacher;
        TextView potok;
        TextView time;

        private PairViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            group = itemView.findViewById(R.id.group);
            pair = itemView.findViewById(R.id.pair);
            discipline = itemView.findViewById(R.id.discipline);
            vid = itemView.findViewById(R.id.vid);
            aud = itemView.findViewById(R.id.aud);
            teacher = itemView.findViewById(R.id.teacher);
            potok = itemView.findViewById(R.id.potok);
            time = itemView.findViewById(R.id.time);

        }
    }

    @NonNull
    @Override
    public PairViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_pair,viewGroup,false);
        PairViewHolder viewHolder = new PairViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PairViewHolder holder, int i) {
        PairItem item = data.get(i);
        holder.group.setText(item.getGRUP());
        holder.pair.setText(item.getPAIR());
        holder.discipline.setText(item.getDISCIPLINE());
        holder.vid.setText(item.getVID());
        holder.aud.setText(item.getAUD());
        holder.teacher.setText(item.getTEACHER());
        holder.potok.setText(item.getPOTOK());
        holder.time.setText(item.getTIME());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}