package com.example.weather.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weather.model.DataWeather;
import com.example.weather.R;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PageFragment extends Fragment {
    @BindView(R.id.text_celsius) TextView mText_celsius;
    @BindView(R.id.txt_sunrise_time) TextView mText_sunrise;
    @BindView(R.id.txt_sunset_time) TextView mText_sunset;
    @BindView(R.id.text_temp_min) TextView mText_temp_min;
    @BindView(R.id.text_temp_max) TextView mText_temp_max;
    @BindView(R.id.text_descripton) TextView mText_description;
    @BindView(R.id.text_visibility) TextView mText_visibility;
    @BindView(R.id.text_speed_wind) TextView mText_wind_speed;
    @BindView(R.id.image_icon) ImageView mImage_icon;
    @BindView(R.id.image_background) ImageView mImage_background;
    @BindView(R.id.progress_view_humidity) CircularProgressView mCircular_progress_humidity;
    @BindView(R.id.progress_view_clouds) CircularProgressView mCircular_progress_clouds;
    @BindView(R.id.progress_view_pressure) CircularProgressView mCircular_progress_pressure;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            DataWeather mDate = (DataWeather) getArguments().getSerializable("dataWeather");
            sunStation(mDate);
            setIcon(mDate.getIcon());
            mText_celsius.setText(String.valueOf(mDate.getTemperature()));
            mText_visibility.setText(mDate.getVisibility());
            mText_temp_min.setText(String.valueOf(mDate.getTemp_min()));
            mText_temp_max.setText(String.valueOf(mDate.getTemp_max()));
            mText_description.setText(mDate.getDescription());
            mText_wind_speed.setText(String.valueOf(mDate.getSpeed()));
            mCircular_progress_humidity.setProgress(mDate.getHumidity(), true);
            mCircular_progress_clouds.setProgress(mDate.getClouds(), true);
            mCircular_progress_pressure.setProgress(mDate.getPressure(), true);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    void sunStation(DataWeather dataWeather) {
        DateFormat formatter = new SimpleDateFormat(" HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dataWeather.getSunrise() * 1000);
        mText_sunrise.setText(formatter.format(calendar.getTime()));
        calendar.setTimeInMillis(dataWeather.getSunset() * 1000);
        mText_sunset.setText(formatter.format(calendar.getTime()));
    }

    void setIcon(int icon) {
        switch (icon) {
            case 1:
                setResource(R.drawable.icon_d1, R.drawable.clear_sky_01d);
                break;
            case 2:
                setResource(R.drawable.icon_d2, R.drawable.few_clouds_02d);
                break;
            case 3:
                setResource(R.drawable.icon_3dn, R.drawable.scattered_clouds_03d);
                break;
            case 4:
                setResource(R.drawable.icon_3dn, R.drawable.clouds_04d);
                break;
            case 9:
                setResource(R.drawable.icon_9dn, R.drawable.rain_09d);
                break;
            case 10:
                setResource(R.drawable.icon_10d, R.drawable.rain_10d);
                break;
            case 11:
                setResource(R.drawable.icon_11dn, R.drawable.thunderstorm_11);
                break;
            case 13:
                setResource(R.drawable.icon_13dn, R.drawable.snow_13d);
                break;
            case 50:
                setResource(R.drawable.icon_50dn, R.drawable.mist_50);
                break;
            case -1:
                setResource(R.drawable.icon_n1, R.drawable.clear_sky_01n);
                break;
            case -2:
                setResource(R.drawable.icon_n2, R.drawable.few_clouds_02n);
                break;
            case -3:
                setResource(R.drawable.icon_3dn, R.drawable.few_clouds_02n);
                break;
            case -4:
                setResource(R.drawable.icon_3dn, R.drawable.clouds_04n);
                break;
            case -9:
                setResource(R.drawable.icon_9dn, R.drawable.rain_10n);
                break;
            case -10:
                setResource(R.drawable.icon_10n, R.drawable.rain_10n);
                break;
            case -11:
                setResource(R.drawable.icon_11dn, R.drawable.thunderstorm_11);
                break;
            case -13:
                setResource(R.drawable.icon_13dn, R.drawable.snow_13n);
                break;
            case -50:
                setResource(R.drawable.icon_50dn, R.drawable.mist_50);
                break;
        }
    }

    private void setResource(int icon, int background){
        mImage_icon.setImageDrawable(getResources().getDrawable(icon));
        mImage_background.setImageDrawable(getResources().getDrawable(background));
    }
}
