package com.kolotseyd.getweather.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolotseyd.getweather.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyWeatherRecyclerViewAdapter extends RecyclerView.Adapter<DailyWeatherRecyclerViewAdapter.ViewHolder> {

    private final List<Integer> dt;
    private final List<Integer> icons;
    private final List<String> description;
    private final List<Double> temperature;

    private final LayoutInflater layoutInflater;
    private ItemClickListener mClickListener;

    public DailyWeatherRecyclerViewAdapter(Context context, List<Integer> dt, List<Integer> icons, List<String> description, List<Double> temperature) {
        this.dt = dt;
        this.icons = icons;
        this.description = description;
        this.temperature = temperature;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DailyWeatherRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.daily_weather_recyclerview_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherRecyclerViewAdapter.ViewHolder holder, int position) {
        int date = dt.get(position);
        int icon = icons.get(position);
        String desc = description.get(position);
        double temp = temperature.get(position);

        Instant instant = Instant.ofEpochSecond(date);
        Date date1 = Date.from(instant);
        DateFormat df = new SimpleDateFormat("");
        Locale RUSSIAN = new Locale("ru", "RU");

        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("ru")){
            df = DateFormat.getDateInstance(DateFormat.FULL, RUSSIAN);
            String stringDate = df.format(date1);
            holder.textViewRecyclerViewDay.setText(stringDate.substring(0,1).toUpperCase(Locale.ROOT) + stringDate.substring(1,stringDate.length()-8));
        } else {
            df = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH);
            String stringDate = df.format(date1);
            holder.textViewRecyclerViewDay.setText(stringDate.substring(0,1).toUpperCase(Locale.ROOT) + stringDate.substring(1,stringDate.length()-5));
        }

        holder.imageViewRecyclerViewIcons.setImageResource(icon);
        holder.textViewRecyclerViewWeatherDescription.setText(desc.substring(0,1).toUpperCase(Locale.ROOT) + desc.substring(1));
        holder.textViewRecyclerViewWeatherTemperature.setText(String.format("%s°C", Math.round(temp)));
    }

    @Override
    public int getItemCount() {
        return temperature.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textViewRecyclerViewDay;
        ImageView imageViewRecyclerViewIcons;
        TextView textViewRecyclerViewWeatherDescription;
        TextView textViewRecyclerViewWeatherTemperature;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewRecyclerViewDay = itemView.findViewById(R.id.textViewRecyclerViewDay);
            imageViewRecyclerViewIcons = itemView.findViewById(R.id.imageViewRecyclerViewIcons);
            textViewRecyclerViewWeatherDescription = itemView.findViewById(R.id.textViewRecyclerViewWeatherDescription);
            textViewRecyclerViewWeatherTemperature = itemView.findViewById(R.id.textViewRecyclerViewWeatherTemperature);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public String getItem(int id) {
        return description.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
