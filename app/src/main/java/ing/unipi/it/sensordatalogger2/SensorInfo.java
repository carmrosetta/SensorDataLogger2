package ing.unipi.it.sensordatalogger2;

import android.hardware.Sensor;

import java.io.Serializable;

/**
 * Created by carmen on 30/09/14.
 */
public class SensorInfo implements Serializable {
    private int sensorType;
    private String sensorName;
    private int sensorSpeed;
    private float maxRange;


    public SensorInfo(int sensorType, String sensorName, int sensorSpeed, float maxRange) {
        this.sensorType = sensorType;
        this.sensorName = sensorName;
        this.sensorSpeed = sensorSpeed;
        this.maxRange = maxRange;
    }

    public int getSensorType() {
        return sensorType;
    }

    public void setSensorType(int sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public int getSensorSpeed() {
        return sensorSpeed;
    }

    public void setSensorSpeed(int sensorSpeed) {
        this.sensorSpeed = sensorSpeed;
    }

    public float getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(float maxRange) {
        this.maxRange = maxRange;
    }
}
