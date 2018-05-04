package com.zuccessful.a3_mt17065_mc;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity_A3_MT17065 extends AppCompatActivity implements SensorEventListener {

    public void export(MenuItem item) {
        exportToCSV();
    }

    protected DatabaseHelper_A3_MT17065 databaseHelperA3MT17065;
    protected SQLiteDatabase sqLiteDatabase;

    protected SensorManager mSensorManager;
    protected Sensor mSensorAccelerometer;
    protected Sensor mSensorGyroscope;
    protected TextView updateTextValue;

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    protected WifiManager mWifiManager;
    protected BroadcastReceiver mWifiScanReceiver;

    protected TelephonyManager telephonyManager;

    protected MediaRecorder mRecorder;
    int callCount = 0;

    Handler handler = null;
    Runnable r;

    boolean permissionCheck = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        check permissions
        permissionCheck = checkAllPermissions();

        if(!permissionCheck){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO }, 12);
        }

        //connect database
        databaseHelperA3MT17065 = new DatabaseHelper_A3_MT17065(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //get sensors
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                TextView latitude = findViewById(R.id.lat_value);
                TextView longitude = findViewById(R.id.long_value);
                latitude.setText("Lat: " + String.valueOf(location.getLatitude()));
                longitude.setText("Long: " + String.valueOf(location.getLongitude()));

                updateDatabase("gps",
                        String.valueOf(location.getLongitude()) + " " + String.valueOf(location.getLongitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mWifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
//                mWifiManager.startScan();
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    List<ScanResult> mScanResults = mWifiManager.getScanResults();
                    Set<String> wifiSSID = new HashSet<>();
                    // add your logic here
                    for (ScanResult sr : mScanResults) {
                        wifiSSID.add(sr.SSID);
                    }
                    String wifinameDatabase = "";
                    String wifiname = "";
                    for (String s : wifiSSID) {
                        wifiname += s;
                        wifiname += "\n";
                        wifinameDatabase += s;
                        wifinameDatabase += " ";
                    }
                    updateTextValue = findViewById(R.id.ssid);
                    updateTextValue.setText(wifiname);

                    updateDatabase("wifi", wifinameDatabase);
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        callCount++;
        int typeSensor = sensorEvent.sensor.getType();

        String gyro;
        String acc;
        TextView xValue, yValue, zValue;

        if (Sensor.TYPE_ACCELEROMETER == typeSensor) {
            xValue = findViewById(R.id.x_value_a);
            yValue = findViewById(R.id.y_value_a);
            zValue = findViewById(R.id.z_value_a);
            xValue.setText(String.format(Locale.ENGLISH, "x: %.2f", sensorEvent.values[0]));
            yValue.setText(String.format(Locale.ENGLISH, "y: %.2f", sensorEvent.values[1]));
            zValue.setText(String.format(Locale.ENGLISH, "z: %.2f", sensorEvent.values[2]));

            acc = String.valueOf(sensorEvent.values[0])
                    + " " + String.valueOf(sensorEvent.values[1]) + " " + String.valueOf(sensorEvent.values[2]);
            if (callCount % 500 == 0)
                updateDatabase("accelerometer", acc);
        }

        if (Sensor.TYPE_GYROSCOPE == typeSensor) {
            xValue = findViewById(R.id.x_value_g);
            yValue = findViewById(R.id.y_value_g);
            zValue = findViewById(R.id.z_value_g);
            xValue.setText(String.format(Locale.ENGLISH, "x: %.2f", sensorEvent.values[0]));
            yValue.setText(String.format(Locale.ENGLISH, "y: %.2f", sensorEvent.values[1]));
            zValue.setText(String.format(Locale.ENGLISH, "z: %.2f", sensorEvent.values[2]));

            gyro = String.valueOf(sensorEvent.values[0])
                    + " " + String.valueOf(sensorEvent.values[1]) + " " + String.valueOf(sensorEvent.values[2]);

            if (callCount % 500 == 0)
                updateDatabase("gyroscope", gyro);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void startUpdate(View view) {
        if(!permissionCheck){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO }, 12);
        }

        onRecord(true);

        final IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(mWifiScanReceiver, intentFilter);

        //handler for update entries at time intervals
        handler = new Handler();

        r = new Runnable() {
            public void run() {
                network();
                registerReceiver(mWifiScanReceiver, intentFilter);
                double amp = getAmplitude();
//                amp = (20 * Math.log10(amp / 0.1));
                if(Double.isInfinite(amp))
                    amp = 0;
                TextView micTextView = findViewById(R.id.mic_value);
                micTextView.setText(String.valueOf(amp));
                updateDatabase("Microphone", String.valueOf(amp));
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

        setVisibility(View.VISIBLE);

        //register listener
        mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        network();
    }

    public void stopUpdate(View view) {
        handler.removeCallbacks(r);
        show();

        setVisibility(View.GONE);

        //unregister listener
        mSensorManager.unregisterListener(this, mSensorAccelerometer);
        mSensorManager.unregisterListener(this, mSensorGyroscope);
        unregisterReceiver(mWifiScanReceiver);
        onRecord(false);
    }

    //entry in database
    public void updateDatabase(String sensor, String description) {
        sqLiteDatabase = databaseHelperA3MT17065.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Database_A3_MT17065.SENSOR, sensor);
        contentValues.put(Database_A3_MT17065.DESCRIPTION, description);
        long row = sqLiteDatabase.insert(Database_A3_MT17065.TABLE_NAME, null, contentValues);
        Log.d("rows_ankur", String.valueOf(row));
    }

    //
    public void show() {
        SQLiteDatabase db1 = databaseHelperA3MT17065.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                Database_A3_MT17065.TIME_STAMP,
                Database_A3_MT17065.SENSOR,
                Database_A3_MT17065.DESCRIPTION
        };

        Cursor cursor = db1.query(
                Database_A3_MT17065.TABLE_NAME, projection, null, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Database_A3_MT17065.TIME_STAMP));
            String val = cursor.getString(cursor.getColumnIndexOrThrow(Database_A3_MT17065.SENSOR));
            String d = cursor.getString(cursor.getColumnIndexOrThrow(Database_A3_MT17065.DESCRIPTION));
//            itemIds.add(itemId);

//            Toast.makeText(this, name + " " + val, Toast.LENGTH_SHORT).show();
            Log.d("ankur", name + " " + val + " " + d);
        }
        cursor.close();
    }

    public void network() {
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
        Log.d("cellAnk", "here" + String.valueOf(cellInfos.size()));

        Set<String> cellId = new HashSet<>();

        String cellData = "";
        String cellDataDatabase = "";

        for (CellInfo cf : cellInfos) {
            String id = "";
            String typeName = "";
            int type = telephonyManager.getPhoneType();
            if (type == TelephonyManager.PHONE_TYPE_GSM)
                typeName = "GSM";
            else if (type == TelephonyManager.PHONE_TYPE_CDMA)
                typeName = "CDMA";

            if (cf instanceof CellInfoGsm) {
                id = String.valueOf(((CellInfoGsm) cf).getCellIdentity().getCid());

            } else if (cf instanceof CellInfoWcdma) {
                id = String.valueOf(((CellInfoWcdma) cf).getCellIdentity().getCid());

            } else if (cf instanceof CellInfoLte) {
                id = String.valueOf(((CellInfoLte) cf).getCellIdentity().getCi());
            }

            cellId.add(id);
        }

        for (String s : cellId) {
            cellData = cellData + "id: " + s + "\n";
            cellDataDatabase = cellDataDatabase + "id: " + s + "; ";
        }

        updateDatabase("network", cellDataDatabase);

        TextView cellTextView = findViewById(R.id.cell_value);
        cellTextView.setText(cellData);

    }

    //set visibility of textboxes
    public void setVisibility(int type) {
        View layoutGPS = findViewById(R.id.layout_gps_value);
        layoutGPS.setVisibility(type);

        View layoutA = findViewById(R.id.layout_a_value);
        layoutA.setVisibility(type);

        View layoutG = findViewById(R.id.layout_g_value);
        layoutG.setVisibility(type);

        View layoutWifi = findViewById(R.id.layout_wifi_value);
        layoutWifi.setVisibility(type);

        View layoutNet = findViewById(R.id.layout_network_value);
        layoutNet.setVisibility(type);

        View layoutMic = findViewById(R.id.layout_mic_value);
        layoutMic.setVisibility(type);

        FloatingActionButton stopButton = findViewById(R.id.stop_button);
        FloatingActionButton startButton = findViewById(R.id.play_button);

        if (type == View.GONE) {
            stopButton.setVisibility(View.GONE);
            startButton.setVisibility(View.VISIBLE);
        } else {
            stopButton.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.GONE);
        }
    }

    //audio record
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile("/dev/null");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("audio tag", "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public double getAmplitude() {
        double amp = 0;
        if (mRecorder != null) {
            amp = mRecorder.getMaxAmplitude();
            return amp;
//            return (20 * Math.log10(amp / (51805.5336 * 0.00002)));
        } else
            return 0;

    }

    public void exportToCSV() {
        SQLiteDatabase sqLiteDatabaseWriter = databaseHelperA3MT17065.getReadableDatabase();
        Cursor c;
        try {
            Log.d("checking", "enter saving");
            c = sqLiteDatabaseWriter.query(Database_A3_MT17065.TABLE_NAME, new String[]{Database_A3_MT17065.TIME_STAMP, Database_A3_MT17065.SENSOR, Database_A3_MT17065.DESCRIPTION}, null,
                    null, null, null, null);
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "SensorLog.csv";
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile);

            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = c.getCount();
            colcount = c.getColumnCount();
            if (rowcount > 0) {
                c.moveToFirst();

                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {
                        bw.write(c.getColumnName(i) + ",");
                    } else {

                        bw.write(c.getColumnName(i));
                    }
                }
                bw.newLine();

                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);

                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(c.getString(j) + ",");
                        else
                            bw.write(c.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
                Toast.makeText(this, "export complete", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();

        } finally {

        }
    }

    public boolean checkAllPermissions(){
        if( (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            && (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
            && (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for(int result:grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, permissions, 12);
                Toast.makeText(this, "Please allow all permissions first!", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }
    }

    public void clear(MenuItem item) {
        databaseHelperA3MT17065.getWritableDatabase().delete(Database_A3_MT17065.TABLE_NAME, null, null);
        Toast.makeText(this, "data cleared", Toast.LENGTH_SHORT).show();
    }
}










