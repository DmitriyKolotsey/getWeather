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

public class HourlyWeatherRecyclerViewAdapter extends RecyclerView.Adapter<HourlyWeatherRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;

    private final List<Integer> dt;
    private final List<Integer> icons;
    private final List<String> description;
    private final List<Integer> temperature;

    public HourlyWeatherRecyclerViewAdapter(Context context, List<Integer> dt, List<Integer> icons, List<String> description, List<Integer> temperature) {
        this.dt = dt;
        this.icons = icons;
        this.description = description;
        this.temperature = temperature;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HourlyWeatherRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.hourly_weather_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int date = dt.get(position);
        int icon = icons.get(position);
        String desc = description.get(position);
        int temp = temperature.get(position);

        Instant instant = Instant.ofEpochSecond(date);
        Date date1 = Date.from(instant);

        if(position == 0) {
            holder.textViewFragmentRecyclerViewHour.setText(R.string.now);
        } else {
            holder.textViewFragmentRecyclerViewHour.setText(String.format("%S:00", date1.getHours()));
        }
        holder.imageViewFragmentRecyclerViewIcons.setImageResource(icon);
        holder.textViewFragmentRecyclerViewWeatherDescription.setText(desc.substring(0,1).toUpperCase(Locale.ROOT) + desc.substring(1));
        holder.textViewFragmentRecyclerViewWeatherTemperature.setText(String.format("%s°C", temp));
    }

    @Override
    public int getItemCount() {
        return dt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFragmentRecyclerViewHour, textViewFragmentRecyclerViewWeatherDescription, textViewFragmentRecyclerViewWeatherTemperature;
        ImageView imageViewFragmentRecyclerViewIcons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFragmentRecyclerViewHour = itemView.findViewById(R.id.textViewFragmentRecyclerViewHour);
            textViewFragmentRecyclerViewWeatherDescription = itemView.findViewById(R.id.textViewFragmentRecyclerViewWeatherDescription);
            textViewFragmentRecyclerViewWeatherTemperature = itemView.findViewById(R.id.textViewFragmentRecyclerViewWeatherTemperature);
            imageViewFragmentRecyclerViewIcons = itemView.findViewById(R.id.imageViewFragmentRecyclerViewIcons);
        }
    }
}
