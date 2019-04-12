package ru.stairenx.nvsutimetable.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.stairenx.nvsutimetable.R;
import ru.stairenx.nvsutimetable.item.PairItem;

public class PairAdapter  extends RecyclerView.Adapter<PairAdapter.PairViewHolder> {

    private List<PairItem> data;

    private int colorLektciya = R.color.colorVidLekcy;
    private int colorPraktica = R.color.colorVidPr;
    private int colorLab = R.color.colorVidLab;
    private int colorExam = R.color.colorVidEc;

    private int drawPairLactciya = R.drawable.shape_pair_lektciya;
    private int drawPairLabaratory = R.drawable.shape_pair_labaratory;
    private int drawPairPraktika = R.drawable.shape_pair_practika;
    private int drawPairExam = R.drawable.shape_pair_exam;

    private int drawLactciya = R.drawable.shape_lektciya;
    private int drawLabaratory = R.drawable.shape_labaratory;
    private int drawPraktika = R.drawable.shape_praktika;
    private int drawExam = R.drawable.shape_exam;

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
        ImageView imgPairType;
        Context context;

        private PairViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext().getApplicationContext();
            cardView = itemView.findViewById(R.id.cardView);
            imgPairType = itemView.findViewById(R.id.imageViewPair);
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
        setBackgroundColor(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void setBackgroundColor(PairViewHolder holder){
        String vid = holder.vid.getText().toString();
        switch (vid){
            case "Лекция" :
                holder.vid.setBackgroundColor(ContextCompat.getColor(holder.context, colorLektciya));
                holder.pair.setBackground(ContextCompat.getDrawable(holder.context, drawPairLactciya));
                holder.imgPairType.setBackground(ContextCompat.getDrawable(holder.context, drawLactciya));
                break;
            case "Практика" :
                holder.vid.setBackgroundColor(ContextCompat.getColor(holder.context, colorPraktica));
                holder.pair.setBackground(ContextCompat.getDrawable(holder.context, drawPairPraktika));
                holder.imgPairType.setBackground(ContextCompat.getDrawable(holder.context, drawPraktika));
                break;
            case "Лабораторная" :
                holder.vid.setBackgroundColor(ContextCompat.getColor(holder.context, colorLab));
                holder.pair.setBackground(ContextCompat.getDrawable(holder.context, drawPairLabaratory));
                holder.imgPairType.setBackground(ContextCompat.getDrawable(holder.context, drawLabaratory));
                break;
            case "Экзамен" :
                holder.vid.setBackgroundColor(ContextCompat.getColor(holder.context, colorExam));
                holder.pair.setBackground(ContextCompat.getDrawable(holder.context, drawPairExam));
                holder.imgPairType.setBackground(ContextCompat.getDrawable(holder.context, drawExam));
                break;
        }
    }
}