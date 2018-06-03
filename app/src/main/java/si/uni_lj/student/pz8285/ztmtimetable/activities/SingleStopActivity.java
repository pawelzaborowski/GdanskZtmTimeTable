package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import si.uni_lj.student.pz8285.ztmtimetable.R;
import si.uni_lj.student.pz8285.ztmtimetable.parser.HttpHandler;

public class SingleStopActivity extends AppCompatActivity  {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv_single_stop;
    public Boolean favStop = false;
    String favStopId;

    ArrayList<HashMap<String, String>> singleStopList;
    private String todayDate;
    private String stopId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_stop);

         HashMap<String, String> stopMap = (HashMap<String, String>) this.getIntent().getSerializableExtra("stopMap");

         if(stopMap != null){
             Toast.makeText(SingleStopActivity.this, stopMap.get("stopId") ,Toast.LENGTH_LONG).show();
         }
        stopId = stopMap.get("stopId");
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(stopMap.get("stopDesc"));

        singleStopList = new ArrayList<>();
        lv_single_stop = (ListView) findViewById(R.id.list);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = df.format(c);

        new GetStop().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.single_stop_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_do_not_favourite:
                this.favStop = false;
                this.favStopId = this.stopId;
                Toast.makeText(SingleStopActivity.this, R.string.removed_from_fav, Toast.LENGTH_LONG).show();
                this.invalidateOptionsMenu();
                return true;
            case R.id.action_do_favourite:
                this.favStop = true;
                this.favStopId = "";
                Toast.makeText(SingleStopActivity.this, R.string.added_to_fav, Toast.LENGTH_LONG).show();
                this.invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem favItem = menu.findItem(R.id.action_do_not_favourite); // full
        MenuItem notFavItem = menu.findItem(R.id.action_do_favourite);  // empty

        favItem.setVisible(this.favStop);
        notFavItem.setVisible(!this.favStop);

        return true;
    }

    private class GetStop extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(SingleStopActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "http://87.98.237.99:88/delays?stopId=" + stopId;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // JSONObject dateOfData = jsonObj.getJSONObject(todayDate);
                    JSONArray singleStop = jsonObj.getJSONArray("delay");

                    for (int i = 0; i < singleStop.length(); i++) {
                        JSONObject c = singleStop.getJSONObject(i);
                        String id = c.getString("id");
                        Log.i("stopId", stopId);
                        String estimatedTime = c.getString("estimatedTime");
                        String routeId = c.getString("routeId");
                        String theoreticalTime = c.getString("theoreticalTime");

                        HashMap<String, String> stop = new HashMap<>();

                        stop.put("stopId", stopId);
                        stop.put("estimatedTime", estimatedTime);
                        stop.put("routeId", routeId);
                        stop.put("theoreticalTime", theoreticalTime);

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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(SingleStopActivity.this, singleStopList,
                    R.layout.one_stop_item, new String[]{"tripId", "estimatedTime"},
                    new int[]{R.id.tripId, R.id.estimatedTime});
            lv_single_stop.setAdapter(adapter);


        }
    }
}
