package ro.videanuadrian.mobile.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView insideValueText;
    TextView outsideValueText;
    char degree = (char) 0x00B0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insideValueText = (TextView) findViewById(R.id.insideTextView);
        outsideValueText = (TextView) findViewById(R.id.outsideTextView);

        final TextView dateValueText = (TextView) findViewById(R.id.dataTextView);

                Button fetch = (Button) findViewById(R.id.fetchButton);

                fetch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String URL = "http://dobresti.go.ro:9999/smarthome/api/temp";
                        //String URL = "http://192.168.158.7:8080/smarthome/api/temp";

                        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());

                        JsonArrayRequest jar = new JsonArrayRequest(
                                Request.Method.GET,
                                URL,
                                null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {

                                        Map<String,TemperatureDisplayDTO> tempMap = new HashMap<>();

                                        try{
                                            // Loop through the array elements
                                            for(int i=0;i<response.length();i++){
                                                // Get current json object
                                                JSONObject temperatureLog = response.getJSONObject(i);

                                                TemperatureDisplayDTO tdDTO = new TemperatureDisplayDTO();
                                                tdDTO.setTemp(temperatureLog.getDouble("temperature"));
                                                tdDTO.setSensor(temperatureLog.getString("sensorId"));
                                                tdDTO.setDate(temperatureLog.getString("timestamp"));

                                                tempMap.put(temperatureLog.getString("sensorId"),tdDTO);
                                            }

                                            insideValueText.setText(tempMap.get("1").getTemp().toString()+degree+"C");
                                            outsideValueText.setText(tempMap.get("2").getTemp().toString()+degree+"C");

                                            Date df = new Date(Long.parseLong(tempMap.get("1").getDate()));
                                            String vv = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(df);

                                            dateValueText.setText(vv);

                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                       int duration = Toast.LENGTH_LONG;

                                        Toast toast = Toast.makeText(getApplicationContext(), error.toString(), duration);
                                        toast.show();

                                    }
                                });
                        rq.add(jar);
                    }
                });

    }
}
