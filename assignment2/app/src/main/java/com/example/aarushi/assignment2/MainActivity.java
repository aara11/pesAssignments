package com.example.aarushi.assignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import android.app.Activity;
import android.os.HandlerThread;
import android.os.Message;
import static java.lang.Math.sqrt;


class Acceleration_data{
    float x;
    float y;
    float z;
    int index;
    double normal;
    public static int index_static = 0;
    public Acceleration_data(float x,float y,float z){
        this.x=x;
        this.y=y;
        this.z=z;
        this.index=index_static++;
        this.normal=sqrt((x*x)+(y*y)+(z*z));

    }
}
class Magnetic_data{
    float gx;
    float gy;
    float gz;
    int index;
    double normal;
    public static int index_static = 0;
    public Magnetic_data(float x,float y,float z){
        this.gx=x;
        this.gy=y;
        this.gz=z;
        this.index=index_static++;
        this.normal=sqrt((x*x)+(y*y)+(z*z));
    }
}
class Gyroscope_data{
    float gx;
    float gy;
    float gz;
    int index;
    double normal;
    public static int index_static = 0;
    public Gyroscope_data(float x,float y,float z){
        this.gx=x;
        this.gy=y;
        this.gz=z;
        this.index=index_static++;
        this.normal=sqrt((x*x)+(y*y)+(z*z));
    }
}
public class MainActivity extends Activity implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private SensorManager magSensorManager;
    private Sensor mMagnetic;
    private SensorManager gyroSensorManager;
    private Sensor mGyroscope;
    TextView title,tvx,tvy,tvz,tvmx,tvmy,tvmz,tvgx,tvgy,tvgz;
    RelativeLayout layout;
    private String acc;
    private String read_str = "";
    //private final String filepath = "/storage/extSdCard/aa.txt"; ///sdcard/AData/accelaration.txt";
    // public List<accelerationData>Data=new ArrayList<>();
    public List<Acceleration_data> accel_data=new ArrayList<>();
    public List<Magnetic_data> mag_data=new ArrayList<>();
    public List<Gyroscope_data> gyr_data=new ArrayList<>();
    List<Double> normalData =new ArrayList<>(500);
    double filteredData=0;
    List<Double>filteredDataList=new ArrayList<>();
    private float x;
    private float y;
    private float z;
    private float mx;
    private float my;
    private float mz;
    private float gx;
    private float gy;
    private float gz;

    public static final int MSG_DONE = 1;
    public static final int MSG_ERROR = 2;
    public static final int MSG_STOP = 3;

    private boolean mrunning;
    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private Handler uiHandler = new Handler(){
        public void handleMessage(Message msg){
            String str = (String) msg.obj;
            switch (msg.what)
            {
                case MSG_DONE:
                    int num=accel_data.size();
                    Toast.makeText(getBaseContext(), str,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MSG_ERROR:
                    Toast.makeText(getBaseContext(),str,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MSG_STOP:
                    Toast.makeText(getBaseContext(), str,
                            Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //private LineGraphSeries<DataPoint> series_normal,series_normalf,series_normall,series_normalt;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagnetic = magSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyroSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = gyroSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        magSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
        gyroSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        //get layout
    //  layout = (RelativeLayout) findViewById(R.id.parent);
        title = (TextView)findViewById(R.id.name);
        tvx = (TextView)findViewById(R.id.xval);
        tvy = (TextView)findViewById(R.id.yval);
        tvz = (TextView)findViewById(R.id.zval);
        tvmx = (TextView)findViewById(R.id.aval);
        tvmy = (TextView)findViewById(R.id.bval);
        tvmz = (TextView)findViewById(R.id.cval);
        tvgx = (TextView)findViewById(R.id.pval);
        tvgy = (TextView)findViewById(R.id.qval);
        tvgz = (TextView)findViewById(R.id.rval);
        //etshowval = (EditText)findViewById(R.id.showval);
        title.setText("Accel, Magnetic, Gyro sensors");

        mHandlerThread = new HandlerThread("Working Thread");
        mHandlerThread.start();

        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.post(r);
    }

    private Runnable r = new Runnable(){
        @Override
        public void run ()
        {
            while(true)
            {
                if (mrunning)
                {
                    Message msg1 = new Message();
                    try
                    {
                        if( mrunning)
                            //                 WriteFile(filepath,acc);
                            //for(int i=0;i<20;i++){//
                            //int num=accel_data.get(0).index_static;//accel_data.get(num).index,accel_data.get(num).normal
                            //    series_normal.appendData(new DataPoint(5,1), false, 200);
                            //}
                            msg1.what = MSG_DONE;
                        // msg1.obj = "Start to write to SD 'acc.txt'";
                    }
                    catch (Exception e)
                    {
                        msg1.what = MSG_ERROR;
                        msg1.obj = e.getMessage();
                    }
                    uiHandler.sendMessage(msg1);
                }
                else
                {
                    Message msg2 = new Message();
                    msg2.what = MSG_STOP;
                    //System.out.println("aaaaa");
                    msg2.obj = "sensor";
                    uiHandler.sendMessage(msg2);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
/*
    public void onStartClick(View view)
    {
        start();
    }

    /*  public void onStopClick(View view)
      {
          stop();
      }

      public void onReadClick(View view)
      {
          etshowval.setText(ReadFile(filepath));
      }
  *//*
    private synchronized void start()
    {
        mrunning = true;
    }

    /*    private synchronized void stop()
        {
            mrunning = false;
        }
    */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
        {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];

            acc= String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z);
            if (mrunning)
                accel_data.add(new Acceleration_data(x,y,z));
            tvx.setText("accel X = "+ String.valueOf(x));
            tvy.setText("accel Y = "+ String.valueOf(y));
            tvz.setText("accel Z = "+ String.valueOf(z));
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD )
        {
            mx = sensorEvent.values[0];
            my = sensorEvent.values[1];
            mz = sensorEvent.values[2];

            //acc= String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z);
            if (mrunning)
                mag_data.add(new Magnetic_data(mx,my,mz));
            tvmx.setText("mag X = "+ String.valueOf(mx));
            tvmy.setText("mag Y = "+ String.valueOf(my));
            tvmz.setText("mag Z = "+ String.valueOf(mz));
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE )
        {
            gx = sensorEvent.values[0];
            gy = sensorEvent.values[1];
            gz = sensorEvent.values[2];

            //acc= String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z);
            if (mrunning)
                gyr_data.add(new Gyroscope_data(gx,gy,gz));
            tvgx.setText("gyro X = "+ String.valueOf(gx));
            tvgy.setText("gyro Y = "+ String.valueOf(gy));
            tvgz.setText("gyro Z = "+ String.valueOf(gz));
        }
    }
    private float[] getRotationMatrixFromOrientation(float[] o) {
        float[] xM = new float[9];
        float[] yM = new float[9];
        float[] zM = new float[9];

        float sinX = (float)Math.sin(o[1]);
        float cosX = (float)Math.cos(o[1]);
        float sinY = (float)Math.sin(o[2]);
        float cosY = (float)Math.cos(o[2]);
        float sinZ = (float)Math.sin(o[0]);
        float cosZ = (float)Math.cos(o[0]);

        // rotation about x-axis (pitch)
        xM[0] = 1.0f; xM[1] = 0.0f; xM[2] = 0.0f;
        xM[3] = 0.0f; xM[4] = cosX; xM[5] = sinX;
        xM[6] = 0.0f; xM[7] = -sinX; xM[8] = cosX;

        // rotation about y-axis (roll)
        yM[0] = cosY; yM[1] = 0.0f; yM[2] = sinY;
        yM[3] = 0.0f; yM[4] = 1.0f; yM[5] = 0.0f;
        yM[6] = -sinY; yM[7] = 0.0f; yM[8] = cosY;

        // rotation about z-axis (azimuth)
        zM[0] = cosZ; zM[1] = sinZ; zM[2] = 0.0f;
        zM[3] = -sinZ; zM[4] = cosZ; zM[5] = 0.0f;
        zM[6] = 0.0f; zM[7] = 0.0f; zM[8] = 1.0f;

        // rotation order is y, x, z (roll, pitch, azimuth)
        float[] resultMatrix = matrixMultiplication(xM, yM);
        resultMatrix = matrixMultiplication(zM, resultMatrix);
        return resultMatrix;
    }
    private float[] matrixMultiplication(float[] A, float[] B) {
        float[] result = new float[9];

        result[0] = A[0] * B[0] + A[1] * B[3] + A[2] * B[6];
        result[1] = A[0] * B[1] + A[1] * B[4] + A[2] * B[7];
        result[2] = A[0] * B[2] + A[1] * B[5] + A[2] * B[8];

        result[3] = A[3] * B[0] + A[4] * B[3] + A[5] * B[6];
        result[4] = A[3] * B[1] + A[4] * B[4] + A[5] * B[7];
        result[5] = A[3] * B[2] + A[4] * B[5] + A[5] * B[8];

        result[6] = A[6] * B[0] + A[7] * B[3] + A[8] * B[6];
        result[7] = A[6] * B[1] + A[7] * B[4] + A[8] * B[7];
        result[8] = A[6] * B[2] + A[7] * B[5] + A[8] * B[8];

        return result;
    }
    @Override
    protected void onPause()
    {
        mSensorManager.unregisterListener(this);
        Toast.makeText(this, "Unregister accelerometerListener", Toast.LENGTH_LONG).show();
        super.onPause();
    }
}
