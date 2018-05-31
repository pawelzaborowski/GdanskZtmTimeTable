package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import si.uni_lj.student.pz8285.ztmtimetable.R;
import si.uni_lj.student.pz8285.ztmtimetable.parser.HttpHandler;

public class SingleRouteActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv_single_route;
    private String routeId;

    ArrayList<HashMap<String, String>> routeTimeTable;
    private HashMap<String, String> stopsMap = new HashMap<String, String>();
    private String todayDate;


    String inputPattern = "YYYY-MM-DD'T'HH:MM:SS";
    String outputPattern = "HH:MM";
    SimpleDateFormat inputFormat;
    SimpleDateFormat outputFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_route);

        HashMap<String, String> stopsMap = (HashMap<String, String>) this.getIntent().getSerializableExtra("stopsMap");
        HashMap<String, String> routeMap = (HashMap<String, String>) this.getIntent().getSerializableExtra("routeMap");

        if (routeMap != null) {
            Toast.makeText(SingleRouteActivity.this, routeMap.get("tripShortName"), Toast.LENGTH_LONG).show();
        }
        routeId = routeMap.get("routeId");
        routeTimeTable = new ArrayList<>();
        lv_single_route = (ListView) findViewById(R.id.list);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = df.format(c);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(routeMap.get("tripHeadsign"));

        inputFormat = new SimpleDateFormat(inputPattern);
        outputFormat = new SimpleDateFormat(outputPattern);

        Log.i("stopDesc", String.valueOf(stopsMap));

        new GetRouteTimeTable().execute();
    }

    public class GetRouteTimeTable extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(SingleRouteActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "http://87.98.237.99:88/stopTimes?date=" + todayDate + "&routeId=" + routeId;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray route = jsonObj.getJSONArray("stopTimes");

                    for (int i = 0; i < route.length(); i++) {
                        JSONObject c = route.getJSONObject(i);
                        String routeId = c.getString("routeId");
                        String tripId = c.getString("tripId");
                        String arrivalTime = c.getString("arrivalTime");
                        String departure = c.getString("departureTime");
                        String stopId = c.getString("stopId");

                        String stopDesc = null;
                        if(stopsMap.containsKey(stopId)){
                            stopDesc = stopsMap.get(stopId);
                            Log.i("stopDesc", stopDesc);
                        }

                       // String departureTime = departure.substring(departure.lastIndexOf('T'));
                        //String departureTime = departure.substring(Math.max(0, departure.length() - 7));

                        String[] parts = departure.split(".*T.*");
                        String part1 = parts[0]; // 004
                        String departureTime = parts[1];
                        Log.i("PART [0]", parts[0]);
                        Log.i("PART [1]", parts[1]);

//                        Pattern twopart = Pattern.compile(".*T.*");
//                        Matcher m = twopart.matcher(departure);
//                        String departureTime = m.group(2);
//
//                        Log.i("GROOOOOUP1", m.group(1));
//                        Log.i("GROOOOOUP2", m.group(2));
//
                        HashMap<String, String> stop = new HashMap<>();

                        stop.put("routeId", routeId);
                        stop.put("tripId", tripId);
                        stop.put("arrivalTime", arrivalTime);
                        stop.put("departureTime", departureTime);
                        stop.put("stopId", stopId);

                        if(stopDesc != null) {
                            Log.i("stopDesc", stopDesc);
                            stop.put("stopDesc", stopDesc);
                        }
                        routeTimeTable.add(stop);

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

            ListAdapter adapter = new SimpleAdapter(SingleRouteActivity.this, routeTimeTable,
                    R.layout.one_route_item, new String[]{"stopDesc", "arrivalTime"},
                    new int[]{R.id.stopDesc, R.id.departureTime});
            lv_single_route.setAdapter(adapter);

        }


    }


}
