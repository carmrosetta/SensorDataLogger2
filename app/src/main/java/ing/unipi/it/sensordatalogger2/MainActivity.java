package ing.unipi.it.sensordatalogger2;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class MainActivity extends ActionBarActivity {

    NotificationManager notificationManager;

    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    EditText ageEdit;
    EditText heightEdit;
    EditText weightEdit;
    Spinner smartPhonePositionSpinner;

    TextView tvErrors;

    String sex = null;
    String age = null;
    String height = null;
    String weight = null;
    String smartPhonePosition = null;
    String arraySmartPhonePos[];

    LinearLayout sensorActivationList;

    SensorManager sensorManager;
    List<Sensor> sensorList;
    CheckBox[] sensors;
    Spinner[] sensorSamplingSpeeds;
    LinearLayout[] sensorChoices;
    int sensorTypes[];
    ArrayAdapter<CharSequence> [] samplingSpeedArrayAdapter;
    int sensorDelays[];



    public void initSensorActivationList() {
        sensorActivationList = (LinearLayout) findViewById(R.id.sensorListLinearLayout);
        TextView tvSensorList = new TextView(this);
        tvSensorList.setText("Available sensors:");
        tvSensorList.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tvSensorList.setPadding(26,0,16,0);
        tvSensorList.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        sensorActivationList.addView(tvSensorList);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        sensors = new CheckBox[sensorList.size()];

        sensorSamplingSpeeds = new Spinner[sensorList.size()];

        sensorChoices = new LinearLayout[sensorList.size()];

        sensorTypes = new int[sensorList.size()];

        samplingSpeedArrayAdapter = new ArrayAdapter[sensorList.size()];
        sensorDelays = new int[sensorList.size()];

        for(int i = 0; i < sensorList.size(); i++) {

            final int j = i;

            samplingSpeedArrayAdapter[i] = ArrayAdapter.createFromResource(this, R.array.delay_rates_array, android.R.layout.simple_spinner_item);
            samplingSpeedArrayAdapter[i].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sensorChoices[i] = new LinearLayout(this);
            sensorChoices[i].setOrientation(LinearLayout.HORIZONTAL);
            sensorChoices[i].setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

            sensors[i] = new CheckBox(this);
            sensorSamplingSpeeds[i] = new Spinner(this);

            sensorChoices[i].addView(sensors[i]);
            sensors[i].setText(sensorList.get(i).getName());
            sensorTypes[i] = sensorList.get(i).getType();
            sensors[i].setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            sensorChoices[i].addView(sensorSamplingSpeeds[i]);
            sensorSamplingSpeeds[i].setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            sensorSamplingSpeeds[i].setAdapter(samplingSpeedArrayAdapter[i]);
            sensorSamplingSpeeds[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            sensorDelays[j] = SensorManager.SENSOR_DELAY_NORMAL;
                            break;
                        case 1:
                            sensorDelays[j] = SensorManager.SENSOR_DELAY_UI;
                            break;
                        case 2:
                            sensorDelays[j] = SensorManager.SENSOR_DELAY_GAME;
                            break;
                        case 3:
                            sensorDelays[j] = SensorManager.SENSOR_DELAY_FASTEST;
                        default:
                            sensorDelays[j] = SensorManager.SENSOR_DELAY_NORMAL;
                            break;
                    }

                    Toast.makeText(getApplicationContext(), "Sensor "+j+" speed = "+sensorDelays[j], Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            sensorActivationList.addView(sensorChoices[i]);
        }
    }



    public void initGUI() {

        ArrayAdapter<CharSequence> adapter = null;
        arraySmartPhonePos = getResources().getStringArray(R.array.smart_phone_position_array);
        radioSexGroup = (RadioGroup) findViewById(R.id.radio_sex);
        ageEdit = (EditText) findViewById(R.id.age);
        heightEdit = (EditText) findViewById(R.id.height);
        weightEdit = (EditText) findViewById(R.id.weight);

        smartPhonePositionSpinner = (Spinner) findViewById(R.id.smart_phone_position);
        adapter = ArrayAdapter.createFromResource(this, R.array.smart_phone_position_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        smartPhonePositionSpinner.setAdapter(adapter);
        smartPhonePositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smartPhonePosition = arraySmartPhonePos[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tvErrors = (TextView) findViewById(R.id.tvErrors);

        initSensorActivationList();

    }




    public void startSampling(View v) {

        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        sex = radioSexButton.getText().toString();

        age = ageEdit.getText().toString();

        height = heightEdit.getText().toString();

        weight = weightEdit.getText().toString();


        //TODO controlli sull'obbligatorietà dei campi
//        if ((sex.equals("")) || (age.equals("")) || (height.equals("")) || (weight.equals("")) || (sensorPosition.equals("")) ) {
//
//            tvErrors.setText("All fields are required!");
//            tvErrors.setTextColor(Color.RED);
//
//        } else {
//            Intent intent = new Intent(this.getApplicationContext(), SensorsSamplingService.class);
//            User user = new User(sex, age, height, weight);
//            intent.putExtra("User data", user);
//            intent.putExtra("Sensor sampling rate", String.valueOf(sensorDelay));
//            intent.putExtra("Sensor position", sensorPosition);
//
//            Log.d("Data sent to service", "From main activity to service");
//            startService(intent);
//            finish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SensorsSamplingService.serviceRunning) {
            setContentView(R.layout.activity_main);
            initGUI();
        } else {
            setContentView(R.layout.stop_sampling_layout);
        }


    }
}



