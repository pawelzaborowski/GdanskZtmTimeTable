package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.util.Locale;

import si.uni_lj.student.pz8285.ztmtimetable.R;
import si.uni_lj.student.pz8285.ztmtimetable.parser.HttpHandler;

import static java.lang.Integer.valueOf;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String favStopId;
    private String favStopName;
    private String TAG = MainActivity.class.getSimpleName();

    private ArrayList<HashMap<String, String>> tripList;
    private ArrayList<HashMap<String, String>> timeTable;
    ArrayList<HashMap<String, String>> singleStopList;
    private WebView webView;

    private HashMap<String, String> stopsMap;

    private ListView lv_single_stop;

    TextView textView;
    String todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = (WebView) findViewById(R.id.loading_webView);
        webView.loadUrl("file:///android_asset/loading.html");
        webView.setVisibility(View.GONE);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("busAPP");

        FragmentManager fragmentManager = getFragmentManager();
        if(!isNetworkAvailable()){
            NoConnectionFragment noConnectionFragment = new NoConnectionFragment();
            noConnectionFragment.show(fragmentManager, "internet_dialog");

            if(isNetworkAvailable()) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, MainActivity.this.getClass());
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        }
        else{
            new GetData().execute();
        }

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = df.format(c);

        favStopId = "35240";
        favStopName = "";

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        singleStopList = new ArrayList<>();
        lv_single_stop = (ListView) findViewById(R.id.stops_list_main);
     //   lv_time = findViewById(R.id.stops_times_main);

        textView = (TextView) findViewById(R.id.textView4);
        textView.setText(favStopName);

        this.stopsMap = new HashMap<>();
        tripList = new ArrayList<>();
        timeTable = new ArrayList<>();
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    // set locals after changing language
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // left-side drawer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    // open setting menu and choose language
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            return true;
        }
        if (id == R.id.choose_polish_language){
            setLocale("pl");
        }
        if (id == R.id.choose_english_language){
            setLocale("en");
        }
        if (id == R.id.refresh){
            new GetData().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    // all event handler for drawer menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_route) {

            Intent routeActivityIntent = new Intent(this, RouteActivity.class);
            routeActivityIntent.putExtra("routeList", this.tripList);
            routeActivityIntent.putExtra("stopsMap", this.stopsMap);
            startActivity(routeActivityIntent);


        } else if (id == R.id.nav_stops) {

            Intent stopsActivityIntent = new Intent(this, StopsActivity.class);
            stopsActivityIntent.putExtra("timeTable", this.timeTable);
            startActivity(stopsActivityIntent);

        } else if (id == R.id.nav_schema) {

            Intent schemaActivityIntent = new Intent(this, SchemaActivity.class);
            startActivity(schemaActivityIntent);

        } else if (id == R.id.nav_map) {

            Intent mapActivityIntent = new Intent(this, MapActivity.class);
            mapActivityIntent.putExtra("timeTable", this.timeTable);
            startActivity(mapActivityIntent);

        } else if (id == R.id.nav_settings) {

            Intent settingsActivityIntent = new Intent(this, SettingsActivity.class);
            //startActivityForResult(settingsActivityIntent, 1);
            startActivity(settingsActivityIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            webView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();

            String url_stops = "http://91.244.248.19/dataset/c24aa637-3619-4dc2-a171-a23eec8f2172/resource/cd4c08b5-460e-40db-b920-ab9fc93c1a92/download/stops.json";
            String jsonStr_stops = sh.makeServiceCall(url_stops);

            String url_route = "http://91.244.248.19/dataset/c24aa637-3619-4dc2-a171-a23eec8f2172/resource/33618472-342c-4a4a-ba88-a911ec0ad5a7/download/trips.json";
            String jsonStr_route = sh.makeServiceCall(url_route);

            String url_single_stop = "http://87.98.237.99:88/delays?stopId=" + favStopId;
            String jsonStr_ss = sh.makeServiceCall(url_single_stop);

            Log.e(TAG, "Response from url: " + jsonStr_stops);
            Log.e(TAG, "Response from url: " + jsonStr_route);
            Log.e(TAG, "Response from url: " + jsonStr_ss);


            // stops
            if (jsonStr_stops != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr_stops);

                    JSONObject dateOfData = jsonObj.getJSONObject(todayDate);
                    JSONArray stops = dateOfData.getJSONArray("stops");

                    for (int i = 0; i < stops.length(); i++) {
                        JSONObject c = stops.getJSONObject(i);
                        String stopId = c.getString("stopId");
                        String stopDesc = c.getString("stopDesc");

                        if (valueOf(stopId) <= 9999) {
                            stopsMap.put(stopId, stopDesc);
                        }
                    }

                    for (int i = 0; i < stops.length(); i++) {
                        JSONObject c = stops.getJSONObject(i);
                        String stopId = c.getString("stopId");
                        String stopDesc = c.getString("stopDesc");
                        String stopLat = c.getString("stopLat");
                        String stopLon = c.getString("stopLon");

                        HashMap<String, String> stop = new HashMap<>();

                        if (valueOf(stopId) <= 9999) {

                            stop.put("stopId", stopId);
                            stop.put("stopDesc", stopDesc);
                            stop.put("stopLat", stopLat);
                            stop.put("stopLon", stopLon);
                            timeTable.add(stop);
                        }

                        if (stopId.equals(favStopId)) {
                            favStopName = stopDesc;
                        }

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
            }

            // routes
            if (jsonStr_route != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr_route);

                    JSONObject dateOfData = jsonObj.getJSONObject(todayDate);
                    JSONArray trips = dateOfData.getJSONArray("trips");

                    for (int i = 0; i < trips.length(); i++) {
                        JSONObject c = trips.getJSONObject(i);
                        String id = c.getString("id");
                        String tripId = c.getString("tripId");
                        String routeId = c.getString("routeId");
                        String tripHeadsign = c.getString("tripHeadsign");

                        HashMap<String, String> trip = new HashMap<>();


                        if ((valueOf(routeId) < 13) || ((valueOf(routeId) < 300) && (valueOf(routeId) >= 100))) {
                            trip.put("id", id);
                            trip.put("tripId", tripId);
                            trip.put("routeId", routeId);
                            trip.put("tripHeadsign", tripHeadsign);

                            tripList.add(trip);
                        }

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

                // fav bus stop
                if (jsonStr_ss != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr_ss);

                        JSONArray singleStop = jsonObj.getJSONArray("delay");

                        for (int i = 0; i < singleStop.length(); i++) {
                            JSONObject c = singleStop.getJSONObject(i);
                            String id = c.getString("id");
                            String estimatedTime = c.getString("estimatedTime");
                            String tripId = c.getString("tripId");
                            String theoreticalTime = c.getString("theoreticalTime");
                            String headSign = c.getString("headsign");

                            HashMap<String, String> stop = new HashMap<>();

                            stop.put("stopId", id);
                            stop.put("estimatedTime", estimatedTime);
                            stop.put("tripId", tripId);
                            stop.put("theoreticalTime", theoreticalTime);
                            stop.put("headsign", headSign);

                            singleStopList.add(stop);

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
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            webView.setVisibility(View.INVISIBLE);

            Toast.makeText(MainActivity.this, "DONE", Toast.LENGTH_SHORT).show();

            Collections.sort(tripList, new Comparator<HashMap<String, String>>() {
                @Override
                public int compare(HashMap<String, String> s1, HashMap<String, String> s2) {
                    return (Integer.parseInt(s1.get("routeId")) - Integer.parseInt(s2.get("routeId")));
                }
            });

            textView.setText(favStopName);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, singleStopList,
                    R.layout.one_stop_item_main, new String[]{"tripId", "estimatedTime", "headsign"},
                    new int[]{R.id.tripId, R.id.estimatedTime, R.id.route_textView});
            lv_single_stop.setAdapter(adapter);

        }

    }
}
