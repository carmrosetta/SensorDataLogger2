package ing.unipi.it.sensordatalogger2;

import android.hardware.Sensor;

import java.io.Serializable;

/**
 * Created by carmen on 30/09/14.
 */
public class SensorInfo implements Serializable {
    private int sensorType;
    String sensorName;
    private int sensorSpeed;
    Sensor sensor;

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public SensorInfo(int sensorType, String sensorName, int sensorSpeed, Sensor sensor) {
        this.sensorType = sensorType;
        this.sensorName = sensorName;
        this.sensorSpeed = sensorSpeed;
        this.sensor = sensor;

    }

    public int getSensorType() {
        return sensorType;
    }

    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    public int getSensorSpeed() {
        return sensorSpeed;
    }

    public void setSensorSpeed(int sensorSpeed) {
        this.sensorSpeed = sensorSpeed;
    }
}
