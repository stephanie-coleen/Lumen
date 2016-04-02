package com.example.avishkar.lumen;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    TextView valueTextView;
    int colour = 0xffffd700;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        final Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        valueTextView = (TextView) findViewById(R.id.valueTextView);

        if (lightSensor == null)
        {
            Toast.makeText(MainActivity.this, "Device Not Support Light Sensor", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            float max = lightSensor.getMaximumRange();
            sensorManager.registerListener(lightSensorEvenListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void showNotification(View v)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this) ;
        builder.setSmallIcon(R.mipmap.lightbulb);
        builder.setContentTitle("Lumens");
        builder.setContentText("Wow, that's bright! You should turn the lights off.");
        builder.setColor(colour);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    SensorEventListener lightSensorEvenListener = new SensorEventListener()
    {
        //@Override
        private Toast turnoff = null;
        public void onSensorChanged(SensorEvent event)
        {
            int thrs=500;

            if (event.sensor.getType() == Sensor.TYPE_LIGHT)
            {
                final float currentReading = event.values[0];
                valueTextView.setText(String.valueOf(currentReading) + " Lux");

                if(currentReading>thrs)
                {
                    // NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                    turnoff = Toast.makeText(getApplicationContext(),"Please Switch off the Lights",Toast.LENGTH_SHORT);

                    turnoff.show();

                    new CountDownTimer(3000, 1000)
                    {
                        public void onTick(long millisUntilFinished) {turnoff.show();}
                        public void onFinish() {turnoff.show();}
                    }.start();

                    showNotification(null);

                    View b = findViewById(R.id.turnoffmain);
                    b.setVisibility(View.VISIBLE);


                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {
        }
    };
}