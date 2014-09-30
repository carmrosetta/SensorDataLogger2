package ing.unipi.it.sensordatalogger2;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class SensorsSamplingService extends Service implements SensorEventListener {

    public static boolean serviceRunning = false;
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;
    public static final String TAG = SensorsSamplingService.class.getName();

    private NotificationManager notificationManager;

    User user;
    String smartPhonePosition;
    List<SensorInfo> selectedSensorsData;

    public SensorsSamplingService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceRunning = true;

        Bundle extras = intent.getExtras();
        user = (User) extras.get("User data");
        smartPhonePosition = (String) extras.get("SmartPhone position");
        selectedSensorsData = (List<SensorInfo>) extras.getSerializable("Selected sensors");
        Toast.makeText(getApplicationContext(), ""+selectedSensorsData.size(),Toast.LENGTH_LONG).show();

        return 1;
    }

        @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
