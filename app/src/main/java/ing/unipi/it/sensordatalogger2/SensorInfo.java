package ing.unipi.it.sensordatalogger2;

import java.io.Serializable;

/**
 * Created by carmen on 30/09/14.
 */
public class SensorInfo implements Serializable {
    private int sensorType;
    String sensorName;
    private int sensorSpeed;

    public String getSensorName() {
        return sensorName;
    }

    public SensorInfo(int sensorType, String sensorName, int sensorSpeed) {
        this.sensorType = sensorType;
        this.sensorName = sensorName;

        this.sensorSpeed = sensorSpeed;
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
