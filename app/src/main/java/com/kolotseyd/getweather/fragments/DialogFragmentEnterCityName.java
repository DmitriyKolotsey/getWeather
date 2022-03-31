package com.kolotseyd.getweather.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kolotseyd.getweather.R;

public class DialogFragmentEnterCityName extends DialogFragment {

    EditText editTextEnteredCityName;

    public static final String APP_PREFERENCES = "settings";

    SharedPreferences settings;

    public interface CloseFragmentEventListener {
        void closeFragmentEvent(String s);
    }

    CloseFragmentEventListener closeFragmentEventListener;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            closeFragmentEventListener = (CloseFragmentEventListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity + " must implement CloseFragmentEvent");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_enter_city_name, null);
        getDialog().setTitle(R.string.enter_city_name);
        editTextEnteredCityName = v.findViewById(R.id.editTextFragmentEnterCityName);

        settings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Button buttonFragmentPositive = v.findViewById(R.id.buttonFragmentPositive);
        buttonFragmentPositive.setOnClickListener(view -> {

            if (!editTextEnteredCityName.getText().toString().equals("")){
                settings.edit().putString("city", editTextEnteredCityName.getText().toString()).apply();

                closeFragmentEventListener.closeFragmentEvent(settings.getString("city", "Москва"));

                dismiss();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.enter_city_name), Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonFragmentNegative = v.findViewById(R.id.buttonFragmentNegative);
        buttonFragmentNegative.setOnClickListener(view -> {
            if (!settings.getString("city", "London").equals("")){
                dismiss();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.enter_city_name), Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

}
