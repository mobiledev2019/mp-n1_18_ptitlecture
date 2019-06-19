package ptit.com.ptitmanager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.adapter.StatisticalAdapter;
import ptit.com.ptitmanager.object.Statistic;

public class StatisticalActivity extends AppCompatActivity {
    private static final String TAG = "StatisticalActivity";
    private RecyclerView mRecyclerView;
    private StatisticalAdapter mAdapter;
    private List<Statistic> mList;
    private Spinner mSpiner;
    private TextView mTvTimeStatistical;
    private TextView mTotalHour;
    private TextView mTotalMinute;
    private String arr[] = {
            "3 days",
            "this week",
            "this month"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mList = new ArrayList<>();
        mList.add(new Statistic("Monday", "25", "3", "2019", "7", "50"));
        mList.add(new Statistic("Friday", "29", "3", "2019", "4", "0"));
        mList.add(new Statistic("Sunday", "24", "3", "2019", "0", "0"));
        mAdapter = new StatisticalAdapter(this, mList);
        mRecyclerView = findViewById(R.id.rcvTimeStatistical);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter);

        mSpiner = findViewById(R.id.spnStatistical);
        
        mTotalHour = findViewById(R.id.tvTotalHour);
        
        mTotalMinute = findViewById(R.id.tvTotalMinute);

        ArrayAdapter<String> typesScoreAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arr);

        typesScoreAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        mSpiner.setAdapter(typesScoreAdapter);


        mSpiner.setOnItemSelectedListener(new MyProcessEvent());

        mTvTimeStatistical = findViewById(R.id.tvTimeStatistical);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class MyProcessEvent implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            mTvTimeStatistical.setText(arr[position]);
            getStatistical(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void getStatistical(int position){
        switch (position) {
            case 0:
                int hour3days = 0;
                int minute3days = 0;
                mList.clear();
                mList.add(new Statistic("Monday", "25", "3", "2019", "7", "50"));
                mList.add(new Statistic("Friday", "29", "3", "2019", "4", "0"));
                mList.add(new Statistic("Sunday", "24", "3", "2019", "0", "0"));
                for (Statistic statistic : mList) {
                    hour3days += Integer.parseInt(statistic.getHour());
                    minute3days += Integer.parseInt(statistic.getMinute());
                }
                Log.d(TAG, "hour: " + hour3days + ", minute: " + minute3days);
                if(minute3days >= 60){
                    hour3days += minute3days/60;
                    minute3days += 60%minute3days;
                }
                mTotalHour.setText("" + hour3days);
                mTotalMinute.setText("" + minute3days);
                mAdapter = new StatisticalAdapter(StatisticalActivity.this, mList);
                mRecyclerView.setAdapter(mAdapter);
                break;
            case 1:
                int hour1week = 0;
                int minute1week = 0;
                mList.clear();
                mList.add(new Statistic("Monday", "25", "3", "2019", "7", "50"));
                mList.add(new Statistic("Friday", "29", "3", "2019", "4", "0"));
                mList.add(new Statistic("Sunday", "24", "3", "2019", "0", "0"));
                mList.add(new Statistic("Tuesday", "26", "3", "2019", "6", "30"));
                mList.add(new Statistic("Wednesday", "27", "3", "2019", "3", "50"));
                mList.add(new Statistic("Thursday", "28", "3", "2019", "2", "0"));
                mList.add(new Statistic("Saturday", "29", "3", "2019", "0", "0"));
                for (Statistic statistic : mList) {
                    hour1week += Integer.parseInt(statistic.getHour());
                    minute1week += Integer.parseInt(statistic.getMinute());
                }
                Log.d(TAG, "hour: " + hour1week + ", minute: " + minute1week);
                if(minute1week >= 60){
                    hour1week += minute1week/60;
                    minute1week = minute1week%60;
                }
                mTotalHour.setText("" + hour1week);
                mTotalMinute.setText("" + minute1week);
                mAdapter = new StatisticalAdapter(StatisticalActivity.this, mList);
                mRecyclerView.setAdapter(mAdapter);
                break;
            case 2:
                int hour1month = 0;
                int minute1month = 0;
                mList.clear();
                for (int i = 0; i < 4; i++){
                    mList.add(new Statistic("Monday", "25", "3", "2019", "7", "50"));
                    mList.add(new Statistic("Friday", "29", "3", "2019", "4", "0"));
                    mList.add(new Statistic("Sunday", "24", "3", "2019", "0", "0"));
                    mList.add(new Statistic("Tuesday", "26", "3", "2019", "6", "30"));
                    mList.add(new Statistic("Wednesday", "27", "3", "2019", "3", "50"));
                    mList.add(new Statistic("Thursday", "28", "3", "2019", "2", "0"));
                    mList.add(new Statistic("Saturday", "29", "3", "2019", "0", "0"));
                }
                for (Statistic statistic : mList) {
                    hour1month += Integer.parseInt(statistic.getHour());
                    minute1month += Integer.parseInt(statistic.getMinute());
                }
                Log.d(TAG, "hour: " + hour1month + ", minute: " + minute1month);
                if(minute1month >= 60){
                    hour1month += minute1month/60;
                    minute1month = minute1month%60;
                }
                mTotalHour.setText("" + hour1month);
                mTotalMinute.setText("" + minute1month);
                mAdapter = new StatisticalAdapter(StatisticalActivity.this, mList);
                mRecyclerView.setAdapter(mAdapter);
                break;
            default:
                break;

        }
    }
}
