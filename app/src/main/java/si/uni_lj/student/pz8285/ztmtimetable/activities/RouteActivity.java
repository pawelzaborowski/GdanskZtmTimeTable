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

    public class GetRoutes extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(RouteActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String url = "http://91.244.248.19/dataset/c24aa637-3619-4dc2-a171-a23eec8f2172/resource/33618472-342c-4a4a-ba88-a911ec0ad5a7/download/trips.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject dateOfData = jsonObj.getJSONObject(todayDate);
                    JSONArray trips = dateOfData.getJSONArray("trips");

                    for (int i = 0; i < trips.length(); i++) {
                        JSONObject c = trips.getJSONObject(i);
                        String id = c.getString("id");
                        String tripId = c.getString("tripId");
                        String routeId = c.getString("routeId");
                        String tripHeadsign = c.getString("tripHeadsign");
                        String tripShortName = c.getString("tripShortName");
                        String directionId = c.getString("directionId");

                        HashMap<String, String> trip = new HashMap<>();

                        trip.put("id", id);
                        trip.put("tripId", tripId);
                        trip.put("routeId", routeId);
                        trip.put("tripHeadsign", tripHeadsign);
                        trip.put("tripShortName", tripShortName);
                        trip.put("directionId", directionId);

                        tripList.add(trip);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Collections.sort(tripList, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> s1, HashMap<String, String> s2) {
                    return (Integer.parseInt(s1.get("routeId")) - Integer.parseInt(s2.get("routeId")));
                }
            });

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

                            routeIntent.putExtra("stopsMap", stopsMap);
                            routeIntent.putExtra("routeMap", hashmap);
                            startActivity(routeIntent);
                        }
                    });
        }
    }
}