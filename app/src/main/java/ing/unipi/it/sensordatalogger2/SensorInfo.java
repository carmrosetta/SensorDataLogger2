package ing.unipi.it.sensordatalogger2;

import java.io.Serializable;

/**
 * Created by carmen on 30/09/14.
 */
public class SensorInfo implements Serializable {
    private int sensorType;
    private int sensorSpeed;

    public SensorInfo(int sensorType, int sensorSpeed) {
        this.sensorType = sensorType;
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
