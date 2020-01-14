package ro.videanuadrian.mobile.smarthome.data;


import android.provider.BaseColumns;

import java.sql.Timestamp;

public class SmartHomeContract{


    public static final class SensorEntry implements BaseColumns{

        public static final String TABLE_NAME = "sh_sensors";

        public static final String COLUMN_NAME = "name";
    }


    public static final class TemperatureLogEntry implements  BaseColumns{

        public static final String TABLE_NAME="sh_temperature_log";

        public static final String COLUMN_SENSOR_ID = "sensor_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_TEMPERATURE = "temperature";

    }






}


