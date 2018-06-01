package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import si.uni_lj.student.pz8285.ztmtimetable.R;
import si.uni_lj.student.pz8285.ztmtimetable.model.Model;
import si.uni_lj.student.pz8285.ztmtimetable.parser.HttpHandler;

public class RouteActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv_route;

    private ArrayList<HashMap<String, String>> tripList;
    private ArrayList<String> stopId;
    private ArrayList<String> stopDesc;
    public HashMap<String, String> stopsMap = new HashMap<String, String>();
    String todayDate;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        tripList = new ArrayList<>();
        lv_route = (ListView) findViewById(R.id.list);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = df.format(c);

        tripList = (ArrayList<HashMap<String, String>>) this.getIntent().getSerializableExtra("routeList");
        stopsMap = (HashMap<String, String>) this.getIntent().getSerializableExtra("stopsMap");

        ListAdapter adapter = new SimpleAdapter(RouteActivity.this, tripList,
                R.layout.routes_items, new String[]{ "routeId","tripHeadsign"},
                new int[]{R.id.tripId, R.id.tripHeadsign});
        lv_route.setAdapter(adapter);

        lv_route.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent routeIntent = new Intent(RouteActivity.this, SingleRouteActivity.class);
                        HashMap<String, String> hashmap= tripList.get(position);
                        routeIntent.putExtra("routeMap", hashmap);
                        routeIntent.putExtra("stopsMap", stopsMap);
                        startActivity(routeIntent);
                    }
                });
    }


}