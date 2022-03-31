package com.kolotseyd.getweather.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kolotseyd.getweather.MainActivity;
import com.kolotseyd.getweather.R;
import com.kolotseyd.getweather.adapters.DailyWeatherRecyclerViewAdapter;
import com.kolotseyd.getweather.adapters.HourlyWeatherRecyclerViewAdapter;

import java.util.ArrayList;

public class DialogFragmentHourlyWeather extends DialogFragment {

    HourlyWeatherRecyclerViewAdapter adapter;

    ArrayList<Integer> daysForHourlyFragment = new ArrayList<>();
    ArrayList<Integer> iconsForHourlyFragment = new ArrayList<>();
    ArrayList<String> descriptionForHourlyFragment = new ArrayList<>();
    ArrayList<Integer> temperatureForHourlyFragment = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Hourly Weather");
        View v = inflater.inflate(R.layout.fragment_dialog_hourly_weather, null);

        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewFragmentHourlyWeather);
        LinearLayoutManager horizontalLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLinearLayoutManager);
        adapter = new HourlyWeatherRecyclerViewAdapter(getContext(), daysForHourlyFragment, iconsForHourlyFragment, descriptionForHourlyFragment, temperatureForHourlyFragment);
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < getArguments().getIntegerArrayList("days").size(); i++){
            daysForHourlyFragment.add(i, getArguments().getIntegerArrayList("days").get(i));
            iconsForHourlyFragment.add(i, getArguments().getIntegerArrayList("icons").get(i));
            descriptionForHourlyFragment.add(i, getArguments().getStringArrayList("desc").get(i));
            temperatureForHourlyFragment.add(i, getArguments().getIntegerArrayList("temp").get(i));
            adapter.notifyItemChanged(i);
        }

        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
