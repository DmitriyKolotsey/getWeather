package com.kolotseyd.getweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kolotseyd.getweather.adapters.DailyWeatherRecyclerViewAdapter;
import com.kolotseyd.getweather.fragments.DialogFragmentEnterCityName;
import com.kolotseyd.getweather.fragments.DialogFragmentHourlyWeather;
import com.kolotseyd.getweather.fragments.DialogFragmentShowMoreDailyWeather;
import com.kolotseyd.getweather.model.CurrentWeather.CurrentWeatherResponse;
import com.kolotseyd.getweather.model.DailyWeather.DailyWeatherResponse;
import com.kolotseyd.getweather.model.HourlyWeather.HourlyWeatherResponse;
import com.kolotseyd.getweather.service.ApiService;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DialogFragmentEnterCityName.CloseFragmentEventListener, DailyWeatherRecyclerViewAdapter.ItemClickListener {

    TextView textViewMainActivityWeatherTemperature, textViewMainActivityWeatherDescription,
            textViewMainActivityWeatherTemperatureFeelsLike, textViewMainActivityWeatherPressure, textViewMainActivityWeatherHumidity,
            textViewMainActivityWeatherWindSpeed, textViewMainActivityWeatherWindGust,
            textViewMainActivityWeatherClouds;
    ImageView imageViewWeatherIcon;
    Button buttonMainActivityCityInfo;

    DialogFragment enterCityNameDialog;
    DialogFragment showMoreDailyWeatherDialog;
    DialogFragment hourlyWeatherFragment;

    SharedPreferences settings;

    public static final String APP_PREFERENCES = "settings";

    private static final String API_KEY = "b00d066503816b51dea99587b0586751";
    Retrofit retrofit;
    ApiService apiService;

    ArrayList<Integer> daysForRecyclerView = new ArrayList<>();
    ArrayList<Integer> iconsForRecyclerView = new ArrayList<>();
    ArrayList<String> descriptionForRecyclerView = new ArrayList<>();
    ArrayList<Double> temperatureForRecyclerView = new ArrayList<>();
    ArrayList<Double> feelsLikeForDailyFragment = new ArrayList<>();
    ArrayList<Integer> cloudyForDailyFragment = new ArrayList<>();
    ArrayList<Integer> humidityForDailyFragment = new ArrayList<>();
    ArrayList<Integer> pressureForDailyFragment = new ArrayList<>();
    ArrayList<Double> windSpeedForDailyFragment = new ArrayList<>();
    ArrayList<Double> windGustForDailyFragment = new ArrayList<>();

    ArrayList<Integer> daysForHourlyFragment = new ArrayList<>();
    ArrayList<Integer> iconsForHourlyFragment = new ArrayList<>();
    ArrayList<String> descriptionForHourlyFragment = new ArrayList<>();
    ArrayList<Integer> temperatureForHourlyFragment = new ArrayList<>();

    DailyWeatherRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        enterCityNameDialog = new DialogFragmentEnterCityName();
        showMoreDailyWeatherDialog = new DialogFragmentShowMoreDailyWeather();
        hourlyWeatherFragment = new DialogFragmentHourlyWeather();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMainActivityDailyWeather);
        LinearLayoutManager horizontalLinearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLinearLayoutManager);
        adapter = new DailyWeatherRecyclerViewAdapter(this, daysForRecyclerView,iconsForRecyclerView, descriptionForRecyclerView, temperatureForRecyclerView);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon);

        textViewMainActivityWeatherTemperature = findViewById(R.id.textViewMainActivityWeatherTemperature);
        textViewMainActivityWeatherTemperatureFeelsLike = findViewById(R.id.textViewMainActivityWeatherTemperatureFeelsLikeValue);
        textViewMainActivityWeatherPressure = findViewById(R.id.textViewMainActivityWeatherPressureValue);
        textViewMainActivityWeatherHumidity = findViewById(R.id.textViewMainActivityWeatherHumidityValue);
        textViewMainActivityWeatherClouds = findViewById(R.id.textViewMainActivityWeatherCloudsValue);
        textViewMainActivityWeatherWindSpeed = findViewById(R.id.textViewMainActivityWeatherWindSpeedValue);
        textViewMainActivityWeatherWindGust = findViewById(R.id.textViewMainActivityWindGustValue);
        textViewMainActivityWeatherDescription = findViewById(R.id.textViewMainActivityWeatherDescription);

        buttonMainActivityCityInfo = findViewById(R.id.buttonMainActivityCityInfo);
        buttonMainActivityCityInfo.setOnClickListener(view -> showEnterCityNameDialog());

         retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        settings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        requestWeather();

    }

    private void getCurrentWeather(String lang, String cityName){
        apiService.getCurrentWeatherByCityName(lang,cityName, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<CurrentWeatherResponse>() {
                    @Override
                    public void onSuccess(CurrentWeatherResponse currentWeatherResponse) {
                        storeCurrentWeather(currentWeatherResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException error = (HttpException) e;
                        if (error.code() == 404){
                            showEnterCityNameDialog();
                            Toast.makeText(getApplicationContext(), R.string.city_not_found, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void storeCurrentWeather(CurrentWeatherResponse response){
        textViewMainActivityWeatherTemperature.setText(String.format("%s°C", Math.round(response.getMain().getTemp())));
        textViewMainActivityWeatherTemperatureFeelsLike.setText(String.format("%s°C", Math.round(response.getMain().getFeels_like())));
        setNeededIcon(response.getWeather().get(0).getId());
        textViewMainActivityWeatherDescription.setText(response.getWeather().get(0).getDescription().substring(0, 1).toUpperCase(Locale.ROOT)
                + response.getWeather().get(0).getDescription().substring(1));
        textViewMainActivityWeatherPressure.setText(String.format("%s %s",Math.round(response.getMain().getPressure()*0.75),
                getResources().getString(R.string.mmHg)));
        textViewMainActivityWeatherClouds.setText(String.format("%s%s",response.getClouds().getAll(), getResources().getString(R.string.percent)));
        textViewMainActivityWeatherHumidity.setText(String.format("%s%s",response.getMain().getHumidity(), getResources().getString(R.string.percent)));
        textViewMainActivityWeatherWindSpeed.setText(String.format("%s %s", response.getWind().getSpeed(), getResources().getString(R.string.meter_per_sec)));
        textViewMainActivityWeatherWindGust.setText(String.format("%s %s", response.getWind().getGust(), getResources().getString(R.string.meter_per_sec)));

        settings.edit().putString("latitude", String.valueOf(response.getCoord().getLat())).apply();
        settings.edit().putString("longitude", String.valueOf(response.getCoord().getLon())).apply();
        Log.d("storeCurrentWeather ","latitude " + settings.getString("latitude", "123") + " longitude " + settings.getString("longitude", "123"));

        buttonMainActivityCityInfo.setText(settings.getString("city", "not found"));

        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("ru")){
            getDailyWeather("ru", String.valueOf(response.getCoord().getLat()), String.valueOf(response.getCoord().getLon()));
            adapter.notifyDataSetChanged();
            getHourly("ru",String.valueOf(response.getCoord().getLat()), String.valueOf(response.getCoord().getLon()));
        } else {
            getDailyWeather("en",String.valueOf(response.getCoord().getLat()), String.valueOf(response.getCoord().getLon()));
            adapter.notifyDataSetChanged();
            getHourly("en",String.valueOf(response.getCoord().getLat()), String.valueOf(response.getCoord().getLon()));
        }

    }

    private void getDailyWeather(String lang, String latitude, String longitude){
        apiService.getDailyWeatherByCoordinate(lang,latitude,longitude, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<DailyWeatherResponse>() {
                    @Override
                    public void onSuccess(DailyWeatherResponse dailyWeatherResponse) {
                        Log.d("getDailyWeather ","latitude " + latitude + " longitude " + longitude);
                        storeDailyWeather(dailyWeatherResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException error = (HttpException) e;
                        if (error.code() == 404){
                            showEnterCityNameDialog();
                            Toast.makeText(getApplicationContext(), R.string.city_not_found, Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void storeDailyWeather(DailyWeatherResponse response){
        clearDailyArrayLists();

        for (int i = 1; i < response.getDaily().size(); i++){
            daysForRecyclerView.add(response.getDaily().get(i).getDt());
            descriptionForRecyclerView.add(response.getDaily().get(i).getWeather().get(0).getDescription());
            temperatureForRecyclerView.add(response.getDaily().get(i).getTemp().getDay());

            feelsLikeForDailyFragment.add(response.getDaily().get(i).getFeels_like().getDay());
            cloudyForDailyFragment.add(response.getDaily().get(i).getClouds());
            humidityForDailyFragment.add(response.getDaily().get(i).getHumidity());
            pressureForDailyFragment.add(response.getDaily().get(i).getPressure());
            windSpeedForDailyFragment.add(response.getDaily().get(i).getWind_speed());
            windGustForDailyFragment.add(response.getDaily().get(i).getWind_gust());

            int substringId = Integer.parseInt(Integer.toString(response.getDaily().get(i).getWeather().get(0).getId()).substring(0, 1));

            if (substringId == 2){
                iconsForRecyclerView.add(R.drawable.ic_storm_weather);
            } else if (substringId == 3 || substringId == 5){
                iconsForRecyclerView.add(R.drawable.ic_rainy_weather);
            } else if (substringId == 6){
                iconsForRecyclerView.add(R.drawable.ic_snow_weather);
            } else if (substringId == 7){
                iconsForRecyclerView.add(R.drawable.ic_dust_weather);
            } else if (response.getDaily().get(i).getWeather().get(0).getId() == 800){
                iconsForRecyclerView.add(R.drawable.ic_clear_day);
            } else {
                iconsForRecyclerView.add(R.drawable.ic_cloudy_weather);
            }
            adapter.notifyItemChanged(i-1);
        }
        adapter.notifyDataSetChanged();
    }

    private void getHourly(String lang, String latitude, String longitude){
        apiService.getHourlyWeatherByCoordinate(lang,latitude,longitude, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<HourlyWeatherResponse>() {
                    @Override
                    public void onSuccess(HourlyWeatherResponse hourlyWeatherResponse) {
                        storeHourlyWeather(hourlyWeatherResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException error = (HttpException) e;
                        if (error.code() == 404){
                            showEnterCityNameDialog();
                            Toast.makeText(getApplicationContext(), R.string.city_not_found, Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void storeHourlyWeather(HourlyWeatherResponse response){
        clearHourlyArrayLists();

        for (int i = 0; i < response.getHourly().size(); i++){
            daysForHourlyFragment.add(response.getHourly().get(i).getDt());
            descriptionForHourlyFragment.add(response.getHourly().get(i).getWeather().get(0).getDescription());
            temperatureForHourlyFragment.add((int) Math.round(response.getHourly().get(i).getTemp()));

            int substringId = Integer.parseInt(Integer.toString(response.getHourly().get(i).getWeather().get(0).getId()).substring(0, 1));

            if (substringId == 2){
                iconsForHourlyFragment.add(R.drawable.ic_storm_weather);
            } else if (substringId == 3 || substringId == 5){
                iconsForHourlyFragment.add(R.drawable.ic_rainy_weather);
            } else if (substringId == 6){
                iconsForHourlyFragment.add(R.drawable.ic_snow_weather);
            } else if (substringId == 7){
                iconsForHourlyFragment.add(R.drawable.ic_dust_weather);
            } else if (response.getHourly().get(i).getWeather().get(0).getId() == 800){
                iconsForHourlyFragment.add(R.drawable.ic_clear_day);
            } else {
                iconsForHourlyFragment.add(R.drawable.ic_cloudy_weather);
            }
        }
    }

    private void clearDailyArrayLists(){
        daysForRecyclerView.clear();
        descriptionForRecyclerView.clear();
        temperatureForRecyclerView.clear();
        feelsLikeForDailyFragment.clear();
        cloudyForDailyFragment.clear();
        humidityForDailyFragment.clear();
        pressureForDailyFragment.clear();
        windSpeedForDailyFragment.clear();
        windGustForDailyFragment.clear();
        iconsForRecyclerView.clear();
    }

    private void clearHourlyArrayLists(){
        daysForHourlyFragment.clear();
        iconsForHourlyFragment.clear();
        descriptionForHourlyFragment.clear();
        temperatureForHourlyFragment.clear();

    }

    private void showEnterCityNameDialog(){
        enterCityNameDialog.show(getSupportFragmentManager(), "enterCityDialog");
    }

    private void setNeededIcon(int id){

        int substringId = Integer.parseInt(Integer.toString(id).substring(0, 1));
        
        if (substringId == 2){
            imageViewWeatherIcon.setImageResource(R.drawable.ic_storm_weather);
        } else if (substringId == 3 || substringId == 5){
            imageViewWeatherIcon.setImageResource(R.drawable.ic_rainy_weather);
        } else if (substringId == 6){
            imageViewWeatherIcon.setImageResource(R.drawable.ic_snow_weather);
        } else if (substringId == 7){
            imageViewWeatherIcon.setImageResource(R.drawable.ic_dust_weather);
        } else if (id == 800){
            imageViewWeatherIcon.setImageResource(R.drawable.ic_clear_day);
        } else {
            imageViewWeatherIcon.setImageResource(R.drawable.ic_cloudy_weather);
        }
    }

    @Override
    public void closeFragmentEvent(String s) {

        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("ru")){
            requestWeather();
        } else {
            requestWeather();
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundleWeatherForDailyFragment = new Bundle();

        bundleWeatherForDailyFragment.putInt("day", daysForRecyclerView.get(position));
        bundleWeatherForDailyFragment.putDouble("temp", temperatureForRecyclerView.get(position));
        bundleWeatherForDailyFragment.putDouble("feels_like", feelsLikeForDailyFragment.get(position));
        bundleWeatherForDailyFragment.putInt("icon", iconsForRecyclerView.get(position));
        bundleWeatherForDailyFragment.putString("desc", descriptionForRecyclerView.get(position));
        bundleWeatherForDailyFragment.putInt("cloudy", cloudyForDailyFragment.get(position));
        bundleWeatherForDailyFragment.putInt("humidity", humidityForDailyFragment.get(position));
        bundleWeatherForDailyFragment.putInt("pressure", pressureForDailyFragment.get(position));
        bundleWeatherForDailyFragment.putDouble("wind_speed", windSpeedForDailyFragment.get(position));
        bundleWeatherForDailyFragment.putDouble("wind_gust", windGustForDailyFragment.get(position));

        if (getSupportFragmentManager().findFragmentByTag("showMoreDailyWeatherDialog") != null){
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("showMoreDailyWeatherDialog"));
        }
        showMoreDailyWeatherDialog.setArguments(bundleWeatherForDailyFragment);
        showMoreDailyWeatherDialog.show(getSupportFragmentManager().beginTransaction(),"showMoreDailyWeatherDialog");

    }

    public void onClickShowHourlyFragment(View view) {
        Bundle bundleHourlyWeatherForFragment = new Bundle();

        bundleHourlyWeatherForFragment.putIntegerArrayList("days", daysForHourlyFragment);
        bundleHourlyWeatherForFragment.putIntegerArrayList("icons", iconsForHourlyFragment);
        bundleHourlyWeatherForFragment.putStringArrayList("desc", descriptionForHourlyFragment);
        bundleHourlyWeatherForFragment.putIntegerArrayList("temp", temperatureForHourlyFragment);

        if (getSupportFragmentManager().findFragmentByTag("hourlyWeatherFragment") != null){
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("hourlyWeatherFragment"));
        }
        hourlyWeatherFragment.setArguments(bundleHourlyWeatherForFragment);
        hourlyWeatherFragment.show(getSupportFragmentManager(), "hourlyWeatherFragment");

    }

    private void requestWeather(){
        if (settings.getBoolean("firstrun", true)){
            showEnterCityNameDialog();
            settings.edit().putBoolean("firstrun",false).apply();
        } else {
            if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("ru")){
                getCurrentWeather("ru", settings.getString("city", "Москва"));
                adapter.notifyDataSetChanged();
            } else {
                getCurrentWeather("en", settings.getString("city", "London"));
                adapter.notifyDataSetChanged();

            }
        }
    }

}