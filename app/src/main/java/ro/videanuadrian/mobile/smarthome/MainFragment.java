package ro.videanuadrian.mobile.smarthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainFragment extends Fragment {

    private ArrayAdapter<String> mPITemperaturesAdapter;

    @Override
    public void onStart(){
        super.onStart();
        updateTemperatures();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
       // setHasOptionsMenu(true);
    }

  /*  @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateTemperatures();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        mPITemperaturesAdapter = new ArrayAdapter<>(
                                        getActivity(), // The current context (this activity)
                                        R.layout.list_item_temperatures, // The name of the layout ID.
                                        R.id.list_item_temperatures_textview, // The ID of the textview to populate.
                                        new ArrayList<String>());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = rootView.findViewById(R.id.list_view_temperatures);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String forecast = mPITemperaturesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });


        listView.setAdapter(mPITemperaturesAdapter);
        return rootView;
    }


    private void updateTemperatures(){
        FetchPiDataTask fetchPiDataTask = new FetchPiDataTask();
        fetchPiDataTask.execute(getResources().getString(R.string.URL_API));
    }


    private class FetchPiDataTask extends AsyncTask<String,Void, List<String>> {

        private final String LOG_TAG = FetchPiDataTask.class.getSimpleName();


        private Double convertToUnit(Double temperature,String unitType){

            if (unitType.equals(getString(R.string.pref_unit_value_imperial))){
                temperature = (temperature*1.8)+32;
            }

            return temperature;
        }


        @Override
        protected List<String> doInBackground(String... args) {

            String serverURL = args[0];

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String temperatureJsonStr;

            try {
                URL url = new URL(serverURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                temperatureJsonStr = buffer.toString();

                try {

                /*    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    Boolean gayTest = pref.getBoolean("areyougay",false);
                    Log.e(LOG_TAG,"gayTestu este"+gayTest);*/

                    JSONArray temperaturesJsonArray = new JSONArray(temperatureJsonStr);

                    Map<Long,List<TemperatureDisplayDTO>> tempMap = new HashMap<>();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

                    // Loop through the array elements
                    for (int i = 0; i < temperaturesJsonArray.length(); i++) {
                        // Get current json object
                        JSONObject temperatureLog = temperaturesJsonArray.getJSONObject(i);
                        TemperatureDisplayDTO tdDTO = new TemperatureDisplayDTO();

                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String preferredUnit = pref.getString(getString(R.string.pref_unit_key),getString(R.string.pref_unit_value_metric));

                        Double temp = convertToUnit(temperatureLog.getDouble("temperature"),preferredUnit);

                        tdDTO.setTemp(temp);
                        tdDTO.setSensor(temperatureLog.getString("sensorId"));

                        Long dateLong = Long.parseLong(temperatureLog.getString("timestamp"));
                        Date date = new Date(Long.parseLong(temperatureLog.getString("timestamp")));
                        tdDTO.setDate(sdf.format(date));
                        List<TemperatureDisplayDTO> currentTempList;

                        if (tempMap.get(dateLong)!=null){
                          currentTempList = tempMap.get(dateLong);
                        }else{
                            currentTempList = new ArrayList<>();
                            tempMap.put(dateLong,currentTempList);
                        }
                        currentTempList.add(tdDTO);

                    }

                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    List<String> temperatureAdatorEntries = new ArrayList<>();
                    for(Map.Entry<Long,List<TemperatureDisplayDTO>> entry:tempMap.entrySet()){

                        // always get date from first entry in the list, as the date is the same
                        String currentLine = entry.getValue().get(0).getDate()+" - ";
                        for(TemperatureDisplayDTO tdDTO:entry.getValue()){

                            if (tdDTO.getSensor().equals("1")){
                                currentLine += " In:";
                            }else {
                                currentLine += " Out:";
                            }

                            currentLine += decimalFormat.format(tdDTO.getTemp());
                            currentLine +=" ";
                        }

                        temperatureAdatorEntries.add(currentLine);
                    }
                    return  temperatureAdatorEntries;

                } catch (JSONException jse) {
                    Log.e(LOG_TAG, "Cannot fetch temperature JSON:"+temperatureJsonStr);
                    return new ArrayList<>();
                }


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }


        @Override
        protected void onPostExecute(List<String> temperatures) {
            if (temperatures != null) {
                mPITemperaturesAdapter.clear();
                mPITemperaturesAdapter.addAll(temperatures);
            }
        }
    }


}
