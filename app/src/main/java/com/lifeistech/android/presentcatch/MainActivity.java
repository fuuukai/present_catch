package com.lifeistech.android.presentcatch;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }

    protected void onResume(){
        super.onResume();

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(!sensors.isEmpty()){
            sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    protected void onPause(){
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.d("SensorValues", "\nX軸:" + event.values[0] +
                                  "\nY軸:" + event.values[1] +
                                  "\nZ軸:" + event.values[2]);

            if(gameView.player != null){
                gameView.player.move(-event.values[0]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
