package com.example.pushupcounter;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Creating variables for text view,
    // sensor manager and Proximity sensor.
    TextView Count;
    SensorManager sensorManager;
    Sensor proximitySensor;
    float proximityValue;
    boolean start = false;
    int count =0;
    boolean up = true;
    boolean down = false;
    // sensitivity to tune the difficulty of the push up
    int Psensitivity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Count = findViewById(R.id.Count);
        Button buttonS = (Button) findViewById(R.id.button_start);
        buttonS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // start counting in response to button click
                if(!start){
                start = true;
                buttonS.setText("Stop");
               }
                //stop counting in response to button click
                else if(start){
                    start = false;
                    buttonS.setText("Start");
                }
            }
        });
        Button buttonR = (Button) findViewById(R.id.button_reset);
        buttonR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // resets the counter in response to button click
                count = 0;
                Count.setText(" "+count);
            }
        });
        Count.setText(" "+count);
        // calling sensor service.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // from sensor service we are
        // calling proximity sensor
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        // handling the case if the proximity
        // sensor is not present in users device.
        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // registering our sensor with sensor manager.
            sensorManager.registerListener(proximitySensorEventListener,
                    proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // calling the sensor event class to detect
    // the change in data when sensor starts working.
    SensorEventListener proximitySensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // method to check accuracy changed in sensor.
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // check if the sensor type is proximity sensor.
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                // here we are setting our status to our textview..
                // if sensor event return 0 then object is close
                // to sensor else object is away from sensor.
                proximityValue = event.values[0];

            }
            //if near person is below the limit
            if(proximityValue <= Psensitivity && up && start){
                up = false;
                down = true;
            }
            //if away after near person has done a complete pushup
            else if(proximityValue > Psensitivity && down && start){
                up = true;
                down = false;
                count ++;
                Count.setText(" "+count);
            }
        }
    };
}
