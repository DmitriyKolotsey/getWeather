package com.kolotseyd.getweather.fragments;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kolotseyd.getweather.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

public class DialogFragmentShowMoreDailyWeather extends DialogFragment {

    TextView textViewDailyFragmentWeatherTemperature, textViewDailyFragmentWeatherTemperatureFeelsLikeValue,
            textViewDailyFragmentWeatherDescription, textViewDailyFragmentWeatherCloudsValue, textViewDailyFragmentWeatherPressureValue, textViewDailyFragmentDay,
             textViewDailyFragmentWeatherHumidityValue, textViewDailyFragmentWeatherWindSpeedValue,  textViewDailyFragmentWindGustValue;
    ImageView imageViewDailyFragmentWeatherIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.show_more_daily_weather);
        View v = inflater.inflate(R.layout.fragment_dialog_show_more_daily_weather, null);

        textViewDailyFragmentWeatherTemperature = v.findViewById(R.id.textViewDailyFragmentWeatherTemperature);
        textViewDailyFragmentWeatherTemperatureFeelsLikeValue = v.findViewById(R.id.textViewDailyFragmentWeatherTemperatureFeelsLikeValue);
        textViewDailyFragmentWeatherDescription = v.findViewById(R.id.textViewDailyFragmentWeatherDescription);
        textViewDailyFragmentWeatherCloudsValue = v.findViewById(R.id.textViewDailyFragmentWeatherCloudsValue);
        textViewDailyFragmentWeatherPressureValue = v.findViewById(R.id.textViewDailyFragmentWeatherPressureValue);
        textViewDailyFragmentWeatherHumidityValue = v.findViewById(R.id.textViewDailyFragmentWeatherHumidityValue);
        textViewDailyFragmentWeatherWindSpeedValue = v.findViewById(R.id.textViewDailyFragmentWeatherWindSpeedValue);
        textViewDailyFragmentWindGustValue = v.findViewById(R.id.textViewDailyFragmentWindGustValue);
        textViewDailyFragmentDay = v.findViewById(R.id.textViewDailyFragmentDay);

        imageViewDailyFragmentWeatherIcon = v.findViewById(R.id.imageViewDailyFragmentWeatherIcon);

        setValues();

        return v;
    }

    private void setValues(){
        textViewDailyFragmentWeatherTemperature.setText(String.format("%s°C", Math.round(getArguments().getDouble("temp"))));
        textViewDailyFragmentWeatherTemperatureFeelsLikeValue.setText(String.format("%s°C", Math.round(getArguments().getDouble("feels_like"))));
        textViewDailyFragmentWeatherDescription.setText(getArguments().getString("desc").substring(0,1).toUpperCase(Locale.ROOT)
                + getArguments().getString("desc").substring(1));
        textViewDailyFragmentWeatherCloudsValue.setText(String.format("%s%s", getArguments().getInt("cloudy"), getResources().getString(R.string.percent)));
        textViewDailyFragmentWeatherHumidityValue.setText(String.format("%s%s", getArguments().getInt("humidity"), getResources().getString(R.string.percent)));
        textViewDailyFragmentWeatherPressureValue.setText(String.format("%s %s",Math.round(getArguments().getInt("pressure")*0.75),
                getResources().getString(R.string.mmHg)));
        textViewDailyFragmentWeatherWindSpeedValue.setText(String.format("%s %s", getArguments().getDouble("wind_speed"), getResources().getString(R.string.meter_per_sec)));
        textViewDailyFragmentWindGustValue.setText(String.format("%s %s", getArguments().getDouble("wind_gust"), getResources().getString(R.string.meter_per_sec)));

        imageViewDailyFragmentWeatherIcon.setImageResource(getArguments().getInt("icon"));
        setDate();
    }

    private void setDate(){
        int date = getArguments().getInt("day");
        Instant instant = Instant.ofEpochSecond(date);
        Date date1 = Date.from(instant);
        DateFormat df = new SimpleDateFormat("");
        Locale RUSSIAN = new Locale("ru", "RU");

        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("ru")){
            df = DateFormat.getDateInstance(DateFormat.FULL, RUSSIAN);
            String stringDate = df.format(date1);
            textViewDailyFragmentDay.setText(stringDate.substring(0,1).toUpperCase(Locale.ROOT) + stringDate.substring(1,stringDate.length()-8));
        } else {
            df = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH);
            String stringDate = df.format(date1);
            textViewDailyFragmentDay.setText(stringDate.substring(0,1).toUpperCase(Locale.ROOT) + stringDate.substring(1,stringDate.length()-8));
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
