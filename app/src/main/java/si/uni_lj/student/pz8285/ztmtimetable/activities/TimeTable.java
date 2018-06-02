package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import si.uni_lj.student.pz8285.ztmtimetable.R;

public class TimeTable extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> times;
    private String stopId, stopName;
    private TableLayout tableLayout;
    private TableRow row0, row1, row2 ,row3 ,row4 ,row5 ,row6 ,row7 ,row8 ,row9 ,row10 ,row11 ,row12 ,row13 ,row14 ,
                     row15 ,row16 ,row17 ,row18, row19 ,row20 ,row21 ,row22 ,row23;
    private TextView hour0, hour1 ,hour2 ,hour3 ,hour4 ,hour5 ,hour6 ,hour7 ,hour8 ,hour9 ,hour10 ,
                     hour11 ,hour12 ,hour13 ,hour14 ,hour15 ,hour16 ,hour17 ,hour18 ,hour19 ,hour20 ,hour21,
                     hour22 ,hour23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table);

        times = (ArrayList<HashMap<String, String>>) this.getIntent().getSerializableExtra("timeTable");
        stopName = (String) this.getIntent().getSerializableExtra("stopName");

        StringTokenizer st = new StringTokenizer(stopName, "=");
        String cos = st.nextToken();
        String nextcos = st.nextToken();
        String name = st.nextToken();
        name = name.substring(0, name.length() - 1);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(name);

        this.initUI();


    }

    private void initUI(){
        this.hour0 = findViewById(R.id.hour0);
        this.hour1 = findViewById(R.id.hour1);
        this.hour2 = findViewById(R.id.hour2);
        this.hour3 = findViewById(R.id.hour3);
        this.hour4 = findViewById(R.id.hour4);
        this.hour5 = findViewById(R.id.hour5);
        this.hour6 = findViewById(R.id.hour6);
        this.hour7 = findViewById(R.id.hour7);
        this.hour8 = findViewById(R.id.hour8);
        this.hour9 = findViewById(R.id.hour9);
        this.hour10 = findViewById(R.id.hour10);
        this.hour11 = findViewById(R.id.hour11);
        this.hour12 = findViewById(R.id.hour12);
        this.hour13 = findViewById(R.id.hour13);
        this.hour14 = findViewById(R.id.hour14);
        this.hour15 = findViewById(R.id.hour15);
        this.hour16 = findViewById(R.id.hour16);
        this.hour17 = findViewById(R.id.hour17);
        this.hour18 = findViewById(R.id.hour18);
        this.hour19 = findViewById(R.id.hour19);
        this.hour20 = findViewById(R.id.hour20);
        this.hour21 = findViewById(R.id.hour21);
        this.hour22 = findViewById(R.id.hour22);
        this.hour23 = findViewById(R.id.hour23);

        this.row0 = findViewById(R.id.row_0);
        this.row1 = findViewById(R.id.row_1);
        this.row2 = findViewById(R.id.row_2);
        this.row3 = findViewById(R.id.row_3);
        this.row4 = findViewById(R.id.row_4);
        this.row5 = findViewById(R.id.row_5);
        this.row6 = findViewById(R.id.row_6);
        this.row7 = findViewById(R.id.row_7);
        this.row8 = findViewById(R.id.row_8);
        this.row9 = findViewById(R.id.row_9);
        this.row10 = findViewById(R.id.row_10);
        this.row11 = findViewById(R.id.row_11);
        this.row12 = findViewById(R.id.row_12);
        this.row13 = findViewById(R.id.row_13);
        this.row14 = findViewById(R.id.row_14);
        this.row15 = findViewById(R.id.row_15);
        this.row16 = findViewById(R.id.row_16);
        this.row17 = findViewById(R.id.row_17);
        this.row18 = findViewById(R.id.row_18);
        this.row19 = findViewById(R.id.row_19);
        this.row20 = findViewById(R.id.row_20);
        this.row21 = findViewById(R.id.row_21);
        this.row22 = findViewById(R.id.row_22);
        this.row23 = findViewById(R.id.row_23);

    }

    private void putHours(String hour){

    }

}