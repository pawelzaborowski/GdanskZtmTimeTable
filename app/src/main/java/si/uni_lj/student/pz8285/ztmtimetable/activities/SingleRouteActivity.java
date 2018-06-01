package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.StringTokenizer;

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
    //good

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_route);

        this.stopsMap = (HashMap<String, String>) this.getIntent().getSerializableExtra("stopsMap");
        HashMap<String, String> routeMap = (HashMap<String, String>) this.getIntent().getSerializableExtra("routeMap");

        // this.stopsMap = stopsMap;
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
                        String departureTime = c.getString("departureTime");
                        String stopId = c.getString("stopId");

                        String stopDesc = null;
                        if(stopsMap.containsKey(stopId)){
                            stopDesc = stopsMap.get(stopId);
                            Log.i("stopDesc", stopDesc);
                        }

                        //"1899-12-30T"

                        StringTokenizer st = new StringTokenizer(departureTime, "T");
                        String cos = st.nextToken();
                        departureTime = st.nextToken();


                        HashMap<String, String> stop = new HashMap<>();

                        stop.put("routeId", routeId);
                        stop.put("tripId", tripId);
                        stop.put("arrivalTime", arrivalTime);
                        stop.put("departureTime", departureTime);
                        stop.put("stopId", stopId);

                        if(stopDesc != null) {
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

            ArrayList<HashMap<String, String>> tempTimeTable = new ArrayList<>();
            ArrayList<String> tempTT = new ArrayList<>();
            String firstStop = routeTimeTable.get(0).get("stopId");
            for(int i = 0; i < routeTimeTable.size(); i++){
                if((routeTimeTable.get(i).get("stopId").equals(firstStop)) && i != 0){
                    String stop = routeTimeTable.get(i).get("stopId");
                    //tempTimeTable(i).add(stop);
                    tempTT.add(stop);
                }
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SingleRouteActivity.this,
                    R.layout.one_route_item, tempTT );

            lv_single_route.setAdapter(arrayAdapter);


//            ListAdapter adapter = new SimpleAdapter(SingleRouteActivity.this, routeTimeTable,
//                    R.layout.one_route_item, new String[]{"stopDesc"},
//                    new int[]{R.id.stopDesc});  // , R.id.departureTime
//            lv_single_route.setAdapter(adapter);

//            lv_single_route.setOnItemClickListener(
//                    new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Intent one_route_stops_Intent = new Intent(SingleRouteActivity.this, TimeTable.class);
//                            HashMap<String, String> hashmap= routeTimeTable.get(position);
//
//                            one_route_stops_Intent.putExtra("stopsMap", stopsMap);
//                            one_route_stops_Intent.putExtra("timeTable", routeTimeTable);
//                            startActivity(one_route_stops_Intent);
//                        }
//                    });

        }


    }


}
