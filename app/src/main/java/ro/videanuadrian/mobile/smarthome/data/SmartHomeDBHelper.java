package ro.videanuadrian.mobile.smarthome.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SmartHomeDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION  = 1;

    static final String DATABASE_NAME = "smart_home.db";

    public SmartHomeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_SENSOR_TABLE = "CREATE TABLE " + SmartHomeContract.SensorEntry.TABLE_NAME + " (" +

                SmartHomeContract.SensorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SmartHomeContract.SensorEntry.COLUMN_NAME + " VARCHAR(255) );";

        final String SQL_CREATE_TEMPERATURE_LOG_TABLE = "CREATE TABLE " + SmartHomeContract.TemperatureLogEntry.TABLE_NAME + " (" +

                SmartHomeContract.TemperatureLogEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SmartHomeContract.TemperatureLogEntry.COLUMN_SENSOR_ID + " INTEGER NOT NULL, " +
                SmartHomeContract.TemperatureLogEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                SmartHomeContract.TemperatureLogEntry.COLUMN_TEMPERATURE + " REAL NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + SmartHomeContract.TemperatureLogEntry.COLUMN_SENSOR_ID + ") REFERENCES " +
                SmartHomeContract.SensorEntry.TABLE_NAME + " (" + SmartHomeContract.SensorEntry._ID + ") " +

                "); ";

            db.execSQL(SQL_CREATE_SENSOR_TABLE);
            db.execSQL(SQL_CREATE_TEMPERATURE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + SmartHomeContract.TemperatureLogEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SmartHomeContract.SensorEntry.TABLE_NAME);
        onCreate(db);
    }
}
