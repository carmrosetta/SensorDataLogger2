package ing.unipi.it.sensordatalogger2;

/**
 * Created by carmen on 30/09/14.
 */

import java.io.Serializable;


public class User implements Serializable {
    String sex;
    String age;
    String height;
    String weight;

    public User(String sex, String age, String height, String weight) {
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String toString () {
        return "% User  (sex, age, height[cm], weight[kg]): "+
                sex +", "+ age +", " + height +", " + weight;
    }

}
