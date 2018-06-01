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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import si.uni_lj.student.pz8285.ztmtimetable.R;

public class TimeTable extends AppCompatActivity {

    private HashMap<String, String> times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table);

        times = (HashMap<String, String>) this.getIntent().getSerializableExtra("times");

    }


}