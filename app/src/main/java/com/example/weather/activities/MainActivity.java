package com.example.weather.activities;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather.model.DataWeather;
import com.example.weather.fragment.PageFragment;
import com.example.weather.R;
import com.example.weather.saver.SaveData;
import com.example.weather.adapter.ViewPagerAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

import static com.example.weather.utils.Network.isNetworkAvailable;

public class MainActivity extends AppCompatActivity implements android.support.v7.widget.PopupMenu.OnMenuItemClickListener {
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.btn_add) Button mBtn_add;
    @BindView(R.id.btn_delete) Button mBtn_delete;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.text_city) TextView mTxt_location;
    @BindView(R.id.indicator_page) CircleIndicator mIndicator_page;

    private EditText mEdit_city;
    private ViewPagerAdapter mAdapter;
    private List<DataWeather> mDate = new ArrayList<>();
    private List<String> mCities = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private AlertDialog.Builder mBuilder;
    private final String key_api = "ba11900a9fd7bf7503f3e68670d4d7a4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final SaveData saveData = new SaveData(MainActivity.this);
        mRequestQueue = Volley.newRequestQueue(this);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (isNetworkAvailable(MainActivity.this)) {
            reload(saveData);
        } else {
            Toast.makeText(MainActivity.this, "No connection internet", Toast.LENGTH_SHORT).show();
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mTxt_location.setText(mCities.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_1), getResources().getColor(R.color.swipe_2), getResources().getColor(R.color.swipe_3));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);

                if (isNetworkAvailable(MainActivity.this)) {
                    if (!mCities.isEmpty()) {
                        new CurrentWeather(mCities.get(mViewPager.getCurrentItem()), true).execute();
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem());
                    } else {
                        reload(saveData);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No connection internet", Toast.LENGTH_SHORT).show();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @OnClick(R.id.btn_delete)
    public void delete(View v) {
        if (!mCities.isEmpty()) {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
            popupMenu.setOnMenuItemClickListener(MainActivity.this);
            popupMenu.inflate(R.menu.menu);
            popupMenu.show();
        }
    }
    @OnClick(R.id.btn_add)
    public void add(View v) {
        mBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.dialog_add, null);
        mEdit_city = view.findViewById(R.id.edit_location);
        mBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isNetworkAvailable(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No connection internet", Toast.LENGTH_LONG).show();
                } else {
                    new CurrentWeather(mEdit_city.getText().toString(), false).execute();
                    mSwipeRefreshLayout.setEnabled(true);
                }
            }
        });
        mBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBuilder.setCancelable(true);
            }
        });
        mBuilder.setView(view);
        mBuilder.show().getWindow().setBackgroundDrawableResource(R.drawable.shape);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        mDate.remove(mViewPager.getCurrentItem());
        mCities.remove(mViewPager.getCurrentItem());
        mAdapter.delete(mViewPager.getCurrentItem());
        mIndicator_page.setViewPager(mViewPager);
        if (!mCities.isEmpty()) {
            if (mViewPager.getCurrentItem() == mCities.size() - 1 && mCities.size() != 1) {
                mTxt_location.setText(mCities.get(mCities.size() - 1));
            } else {
                mTxt_location.setText(mCities.get(mViewPager.getCurrentItem()));
            }
        } else {
            mSwipeRefreshLayout.setEnabled(false);
            mTxt_location.setText("");
        }
        return true;
    }

    @Override
    protected void onStop() {
        if (isNetworkAvailable(MainActivity.this)) {
            SaveData saveData = new SaveData(MainActivity.this);
            saveData.saveData(mCities);
        }
        super.onStop();
    }

    void reload(SaveData saveData) {
        for (int i = 0; i < saveData.loadData().size(); i++) {
            new CurrentWeather(saveData.loadData().get(i), false).execute();
        }
    }


    class CurrentWeather extends AsyncTask<Void, Void, Void> {
        private String city_name;
        private Boolean refresh;

        public CurrentWeather(String city_name, Boolean refresh) {
            this.city_name = city_name;
            this.refresh = refresh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url_current = "https://api.openweathermap.org/data/2.5/weather?q=" + city_name + "&units=metric&APPID=" + key_api;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_current, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        DataWeather dataWeather = new DataWeather();
                        PageFragment fragment = new PageFragment();
                        Bundle bundle = new Bundle();

                        JSONObject json_weather = response.getJSONArray("weather").getJSONObject(0);
                        dataWeather.setDescription(json_weather.getString("main"));

                        if (json_weather.getString("icon").contains("d")) {
                            dataWeather.setIcon(Integer.valueOf(json_weather.getString("icon").substring(0, 2)));
                        } else {
                            dataWeather.setIcon(Integer.valueOf(json_weather.getString("icon").substring(0, 2)) * (-1));
                        }

                        JSONObject json_main = response.getJSONObject("main");
                        dataWeather.setTemperature(json_main.getInt("temp"));
                        dataWeather.setTemp_min(json_main.getInt("temp_min"));
                        dataWeather.setTemp_max(json_main.getInt("temp_max"));
                        dataWeather.setPressure(json_main.getInt("pressure"));
                        dataWeather.setHumidity(json_main.getInt("humidity"));
                        if (response.has("visibility")) {
                            dataWeather.setVisibility(response.getString("visibility"));
                        } else {
                            dataWeather.setVisibility("∞");
                        }
                        JSONObject json_wind = response.getJSONObject("wind");
                        dataWeather.setSpeed(json_wind.getDouble("speed"));
                        dataWeather.setClouds(response.getJSONObject("clouds").getInt("all"));
                        JSONObject json_sys = response.getJSONObject("sys");
                        dataWeather.setSunrise(json_sys.getLong("sunrise"));
                        dataWeather.setSunset(json_sys.getLong("sunset"));

                        bundle.putSerializable("dataWeather", dataWeather);
                        fragment.setArguments(bundle);

                        if (refresh) {
                            mAdapter.refresh(fragment, mViewPager.getCurrentItem());
                        } else {
                            mCities.add(response.getString("name"));
                            mAdapter.addFrag(fragment);
                            mIndicator_page.setViewPager(mViewPager);
                            mViewPager.setAdapter(mAdapter);
                            mDate.add(dataWeather);
                            mViewPager.setCurrentItem(mDate.size());
                            mTxt_location.setText(mCities.get(mDate.size() - 1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "check city name maybe isn't correct", Toast.LENGTH_LONG).show();
                }
            });

            mRequestQueue.add(jsonObjectRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}