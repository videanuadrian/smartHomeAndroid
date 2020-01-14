package ro.videanuadrian.mobile.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    char degree = (char) 0x00B0;

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    @Override
    public void onStart(){
        super.onStart();
        Log.e(LOG_TAG,"onStart Main Activity");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.e(LOG_TAG,"onStop Main Activity");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(LOG_TAG,"onResume Main Activity");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e(LOG_TAG,"onPause Main Activity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
        Log.e(LOG_TAG,"onCreate Main Activity");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e(LOG_TAG,"onDestroy Main Activity");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.e(LOG_TAG,"onSaveInstanceState Main Activity");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(LOG_TAG,"onResumeInstanceState Main Activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           startActivity(new Intent(this,SettingsActivity.class));
           return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
