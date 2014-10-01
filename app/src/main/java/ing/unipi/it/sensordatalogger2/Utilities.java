package ing.unipi.it.sensordatalogger2;

/**
 * Created by carmen on 01/10/14.
 */

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Utilities {


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void showNotification (Context context, NotificationManager notificationManager, String message, Class cls) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Sensor Data Logger");
        builder.setContentText("The sensor data logger service is active");
        builder.setContentInfo("These are other info about the service");
        builder.setSmallIcon(R.drawable.emo_im_surprised);
        builder.setTicker(message);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(false);

        builder.setContentTitle(message);
        //TODO pending intent
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, cls), PendingIntent.FLAG_UPDATE_CURRENT);//studiare i flag
        builder.setContentIntent(contentIntent);
        notificationManager.notify(0, builder.build());

    }

    public static String getSensorNameById(int sensorId, String name) {
        String sensorName = "";
        switch(sensorId) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorName = "Accelerometer";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sensorName = "Thermometer";
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                sensorName = "Game rotation sensor";
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                sensorName = "Geomagnetic rotation vector sensor";
                break;
            case Sensor.TYPE_GRAVITY:
                sensorName = "Gravity sensor";
                break;
            case Sensor.TYPE_GYROSCOPE:
                sensorName = "Gyroscope";
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                sensorName = "Uncalibrated gyroscope";
                break;
            case Sensor.TYPE_HEART_RATE:
                sensorName = "Heart rate sensor";
                break;
            case Sensor.TYPE_LIGHT:
                sensorName = "Light sensor";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                sensorName = "Linear acceleration sensor";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorName = "Magnetic field sensor";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                sensorName = "Uncalibrated magnetic field sensor";
                break;
            case Sensor.TYPE_PRESSURE:
                sensorName = "Barometer";
                break;
            case Sensor.TYPE_PROXIMITY:
                sensorName = "Proximity sensor";
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                sensorName = "Relavire humidity sensor";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                sensorName = "Rotation vector sensor";
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                sensorName = "Significant motion sensor";
                break;
            case Sensor.TYPE_STEP_COUNTER:
                sensorName = "Step counter";
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                sensorName = "Step detector";
                break;
            default:
                sensorName = name;
                break;

        }
        return sensorName;
    }




    public static void writeData(File file, String data) {

        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
            fw.close();
        } catch (IOException e) {
        }
    }

    public static File createDirectory(String dirName) {

        File directory = null;

        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getAbsolutePath() ;

        directory =  new File(path, dirName);

        if (!directory.exists()){
            directory.mkdirs();
        }


        return directory;

    }

    public static File createFile(File directory, String fileName) {
        File file = null;

        try{
            file = new File(directory, fileName);
            file.createNewFile();
        } catch (IOException e) {
            Log.e("ERROR", "Exception while creating file:" + e.toString());
        }

        return file;
    }


    public static String getTimeInSeconds(long timeInMillis) {

        int seconds = (int) (timeInMillis / 1000);
        int milliseconds = (int) (timeInMillis % 1000);


        String timeInSeconds = "" + String.format(seconds + "." + String.format("%03d", milliseconds));
        return timeInSeconds;
    }


    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getDeviceName() {
        String deviceName = "";
        String manufacturer = capitalize(Build.MANUFACTURER);
        String model = Build.MODEL;

        if (model.startsWith(manufacturer))
            deviceName = model;
        else deviceName = manufacturer+" "+model;

        return deviceName;
    }
    public static String getAndroidVersion() {
        StringBuilder builder = new StringBuilder();
        builder.append("Android ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append("  (").append(fieldName).append(") , ");
                builder.append("sdk = ").append(fieldValue);
            }
        }
        return builder.toString();

    }

    public static String getDateTimeFromMillis(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static long getFileSize(File file) {
        return file.length()/1024;
    }



}
