package edu.upenn.cis350.workingdogapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DogHistoryActivity extends AppCompatActivity implements Database.DataBaseObserver {
    // This class displays the weight history of a dog in graph form

    private Database.DogEntry dog;
    private Map<Long, Double> weightHistory; // Stores the dog's weight history as a hashmap
    private LineGraphSeries<DataPoint> mSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_history);

        dog = (Database.DogEntry) getIntent().getExtras().getSerializable("dog");
        weightHistory = new HashMap<Long, Double>();
        mSeries = new LineGraphSeries<>();

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.addSeries(mSeries);

        Database.getInstance().getDogWeightHistory(dog, weightHistory, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        RelativeLayout activity_menu;
        getMenuInflater().inflate(R.menu.popupmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        Intent intent = PopupMenu.Select(item.getItemId(), getApplicationContext());
        startActivityForResult(intent, 1);
        return true;
    }

    @Override
    public void onDataBaseChanged() {

        DataPoint[] values = new DataPoint[weightHistory.size()];
        int i = 0;
        Calendar calendar = Calendar.getInstance();
        List<Long> sysMillisTimes = new ArrayList<Long>(weightHistory.keySet());
        Collections.sort(sysMillisTimes);

        for(Long sysMillis : sysMillisTimes){
            Double weight = weightHistory.get(sysMillis);
            calendar.setTimeInMillis(sysMillis);
            Date date = calendar.getTime();

            DataPoint v = new DataPoint(date, weight);
            values[i] = v;
            i++;
        }

        mSeries.resetData(values);
    }
}
