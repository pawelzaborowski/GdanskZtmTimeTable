package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import si.uni_lj.student.pz8285.ztmtimetable.R;
import si.uni_lj.student.pz8285.ztmtimetable.parser.HttpHandler;

public class StopsActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    private String favStopId;
    Intent intent;

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

        stopsList = (ArrayList<HashMap<String, String>>) this.getIntent().getSerializableExtra("stopsList");

        final ListAdapter adapter = new SimpleAdapter(StopsActivity.this, stopsList,
                R.layout.stops_items, new String[]{"stopDesc", "stopId"},
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

    public class GetStops extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(StopsActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "http://91.244.248.19/dataset/c24aa637-3619-4dc2-a171-a23eec8f2172/resource/cd4c08b5-460e-40db-b920-ab9fc93c1a92/download/stops.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject dateOfData = jsonObj.getJSONObject(todayDate);
                    JSONArray stops = dateOfData.getJSONArray("stops");

                    for (int i = 0; i < stops.length(); i++) {
                        JSONObject c = stops.getJSONObject(i);
                        String stopId = c.getString("stopId");
                        String stopShortName = c.getString("stopShortName");
                        String stopDesc = c.getString("stopDesc");
                        String subName = c.getString("subName");
                        String stopLat = c.getString("stopLat");
                        String stopLon = c.getString("stopLon");

                        HashMap<String, String> stop = new HashMap<>();

                        stop.put("stopId", stopId);
                        stop.put("stopShortName", stopShortName);
                        stop.put("stopDesc", stopDesc);
                        stop.put("subName", subName);
                        stop.put("stopLat", stopLat);
                        stop.put("stopLon", stopLon);

                        stopsList.add(stop);

                        //[{stopLat=54.54907, stopShortName=9480, subName=9480, stopLon=18.49896, stopId=39480, stopDesc=Terminal Promowy},

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
            ListAdapter adapter = new SimpleAdapter(StopsActivity.this, stopsList,
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

}