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

public class StopsActivity extends AppCompatActivity {

    private ListView lv;

    ArrayList<HashMap<String, String>> stopsList;
    private String todayDate;

    private EditText etSearch;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        stopsList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        etSearch = (EditText) findViewById(R.id.editText);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = df.format(c);

        stopsList = (ArrayList<HashMap<String, String>>) this.getIntent().getSerializableExtra("timeTable");

        final ListAdapter adapter = new SimpleAdapter(StopsActivity.this, stopsList,
                R.layout.stops_items, new String[]{"stopDesc"},
                new int[]{R.id.stopDesc});
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent stopIntent = new Intent(StopsActivity.this, SingleStopActivity.class);
                        HashMap<String, String> hashmap= stopsList.get(position);
                        stopIntent.putExtra("stopMap", hashmap);
                        startActivity(stopIntent);
                    }
                });

    }

    // https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

    @Override
    protected void onResume() {
        super.onResume();

        final ListAdapter adapter = new SimpleAdapter(StopsActivity.this, stopsList,
                R.layout.stops_items, new String[]{"stopDesc", "stopId"},
                new int[]{R.id.stopDesc});
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent stopIntent = new Intent(StopsActivity.this, SingleStopActivity.class);
                        HashMap<String, String> hashmap = stopsList.get(position);
                        stopIntent.putExtra("stopMap", hashmap);
                        startActivity(stopIntent);
                    }
                });
    }


}