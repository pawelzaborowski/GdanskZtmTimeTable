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
//
//    private String TAG = MainActivity.class.getSimpleName();
//    private ListView lv;
//
//    private String favStopId;
//    Intent intent;
//
//    ArrayList<HashMap<String, String>> timeTable;
//    private HashMap<String, String> stopsMap = new HashMap<String, String>();
//    ArrayList<HashMap<String, String>> stopsList;
//    private String todayDate;
//
//    private EditText etSearch;
//
//    @SuppressLint("SimpleDateFormat")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//   //     setContentView(R.layout.stops_list);
//
//        timeTable = new ArrayList<>();
//        lv = (ListView) findViewById(R.id.list);
//        etSearch = (EditText) findViewById(R.id.editText);
//
//        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        todayDate = df.format(c);
//
//        timeTable = (ArrayList<HashMap<String, String>>) this.getIntent().getSerializableExtra("timeTable");
//        this.stopsMap = (HashMap<String, String>) this.getIntent().getSerializableExtra("stopsMap");
//
//
//        final ListAdapter adapter = new SimpleAdapter(TimeTable.this, timeTable,
//    //            R.layout.stops_list_items, new String[]{"stopDesc"},
//                new int[]{R.id.stopDesc});
//        lv.setAdapter(adapter);
//
//        lv.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent stopIntent = new Intent(TimeTable.this, StopTimeTableActivity.class);
//                        HashMap<String, String> hashmap= timeTable.get(position);
//                        stopIntent.putExtra("timeTable", hashmap);
//                        startActivity(stopIntent);
//                    }
//                });
//
//    }
//
//    // https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
//
////    @Override
////    protected void onResume() {
////        super.onResume();
////
////        final ListAdapter adapter = new SimpleAdapter(StopsActivity.this, timeTable,
////                R.layout.stops_items, new String[]{"stopDesc", "stopId"},
////                new int[]{R.id.stopDesc});
////        lv.setAdapter(adapter);
////
////        lv.setOnItemClickListener(
////                new AdapterView.OnItemClickListener() {
////                    @Override
////                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                        Intent stopIntent = new Intent(StopsActivity.this, SingleStopActivity.class);
////                        HashMap<String, String> hashmap = timeTable.get(position);
////                        stopIntent.putExtra("stopMap", hashmap);
////                        startActivity(stopIntent);
////                    }
////                });
////    }
////

}