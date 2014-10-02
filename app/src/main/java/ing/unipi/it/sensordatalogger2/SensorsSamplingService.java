package ing.unipi.it.sensordatalogger2;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class SensorsSamplingService extends Service implements SensorEventListener {

    public static boolean serviceRunning = false;
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;
    public static final String TAG = SensorsSamplingService.class.getName();

    private PowerManager.WakeLock mWakeLock = null;

    long[] lastUpdate;
    long[] count;
    long[] varTime;

    int activeSensors = 0;

    File[] samplesDirectories;
    File[] samplesFiles;

    SensorManager sensorManager;

    private NotificationManager notificationManager;

    User user;
    String smartPhonePosition;
    List<SensorInfo> selectedSensorsData;

    public SensorsSamplingService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("service started", "service started");

        serviceRunning = true;

        Bundle extras = intent.getExtras();
        user = (User) extras.get("User data");
        smartPhonePosition = (String) extras.get("SmartPhone position");
        selectedSensorsData = (List<SensorInfo>) extras.getSerializable("Selected sensors");

        activeSensors = selectedSensorsData.size();
        //Toast.makeText(getApplicationContext(), ""+activeSensors,Toast.LENGTH_LONG).show();

        samplesDirectories = new File[activeSensors];
        samplesFiles = new File[activeSensors];

        lastUpdate = new long[activeSensors];
        count = new long[activeSensors];
        varTime = new long[activeSensors];

        long todayDate = System.currentTimeMillis();
        String startDate = Utilities.getDateTimeFromMillis(todayDate, "yy-MM-dd");
        String startTime = Utilities.getDateTimeFromMillis(todayDate, "kk-mm-ss");
        String device = Utilities.getDeviceName();
        String androidVersion = Utilities.getAndroidVersion();


        for(int i = 0; i < activeSensors; i++) {

            String sensorName = Utilities.getSensorNameById(selectedSensorsData.get(i).getSensorType(), selectedSensorsData.get(i).getSensorName());
            String androidSamplingRate = Utilities.getAndroidSamplingRateById(selectedSensorsData.get(i).getSensorSpeed());

            samplesDirectories[i] = Utilities.createDirectory("Samples/"+sensorName+
                    "/"+Utilities.getDateTimeFromMillis(todayDate, "yy-MM-dd"));
            samplesFiles[i] = Utilities.createFile(samplesDirectories[i],Utilities.getDateTimeFromMillis(todayDate, "kk-mm")+".arff");

            if(Utilities.getFileSize(samplesFiles[i]) == 0){

                //meta-data
                Utilities.writeData(samplesFiles[i], "% "+sensorName+" Track\n%\n");
                Utilities.writeData(samplesFiles[i], "% Start Date [YY-MM-DD]: "+startDate+"\n");
                Utilities.writeData(samplesFiles[i], "% Start Time [hh-mm-ss]: "+startTime+"\n%\n");
                Utilities.writeData(samplesFiles[i], "% Device: "+device+"\n");
                Utilities.writeData(samplesFiles[i], "% Android Version: "+androidVersion+"\n");
                Utilities.writeData(samplesFiles[i], "% Range "+Utilities.getSensorUnitById(selectedSensorsData.get(i).getSensorType())+": "+selectedSensorsData.get(i).getMaxRange()+"\n");
                Utilities.writeData(samplesFiles[i], "% Android Sampling Rate: "+androidSamplingRate+"\n%\n");
                Utilities.writeData(samplesFiles[i], user.toString()+"\n");
                Utilities.writeData(samplesFiles[i], "% SmartPhone Position: "+smartPhonePosition+"\n% Notes:\n");
            }

       }

        registerListeners();

        mWakeLock.acquire();

        return START_REDELIVER_INTENT;
    }

    private void registerListeners() {

        for(SensorInfo s : selectedSensorsData) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(s.getSensorType()), s.getSensorSpeed());
        }

    }


    private void unregisterListener() {
        sensorManager.unregisterListener(this);
    }


    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

//todo fare il broadcast receiver
        registerReceiver(actionScreenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));


        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Utilities.showNotification(this, notificationManager, "Service running", MainActivity.class);


    }

    public void onDestroy() {
        unregisterReceiver(actionScreenOffReceiver);

        unregisterListener();

        mWakeLock.release();

        notificationManager.cancelAll();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        for(int i = 0; i < activeSensors; i++) {
            if( event.sensor.getType() == selectedSensorsData.get(i).getSensorType()) {
                varTime[i] = System.currentTimeMillis();

                float[] values = event.values;

                if(lastUpdate[i] == 0) {
                    lastUpdate[i] = varTime[i];
                }

                long diff = varTime[i] - lastUpdate[i];
                count[i] += diff;

                String timestamp = Utilities.getTimeInSeconds(count[i]);

                String sensedValues = "";

                for(int j = 0; j < values.length; j++) {
                    sensedValues += ", "+values[j];
                }

                //sensedValues += "\n";

                Utilities.writeData(samplesFiles[i],timestamp+sensedValues+"\n");

            }
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


//    public void getSensorData(SensorEvent event, long sampleTime, int i) {
//
//    }

    public BroadcastReceiver actionScreenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                return;
            }
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    unregisterListener();
                    registerListeners();
                    notificationManager.cancelAll();
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Utilities.showNotification(getApplicationContext(), notificationManager, "Service running", MainActivity.class);

                }
            };

            new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);

        }
    };

}
