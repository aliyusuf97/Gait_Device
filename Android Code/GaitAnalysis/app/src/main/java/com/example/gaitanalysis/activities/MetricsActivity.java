package com.example.gaitanalysis.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaitanalysis.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import Chart.RealTimeLineChart;
import bluetooth.BluetoothConstants;
import bluetooth.BluetoothParcelable;
import bluetooth.ConnectThread;
import bluetooth.HandlePacket;
import bluetooth.HandlerCallback;

public class MetricsActivity extends AppCompatActivity implements HandlerCallback, OnChartValueSelectedListener, Runnable {

    public BluetoothDevice leftBTDevice;
    public BluetoothDevice rightBTDevice;
    private BluetoothParcelable parcelable;
    private BluetoothAdapter bluetoothAdapter;
    private HandlePacket leftPacketHandler;
    private HandlePacket rightPacketHandler;
    private LineChart leftChart;
    private LineChart rightChart;
    private RealTimeLineChart leftRealTime;
    private RealTimeLineChart rightRealTime;
    private static final int PERIOD=100;
    private View root=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics);
        root=findViewById(android.R.id.content);

        Intent intent = getIntent();
        parcelable = intent.getParcelableExtra("Devices");
        leftBTDevice = parcelable.getLeftDevice();
        rightBTDevice = parcelable.getRightDevice();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        enableBluetoothAdapter();

        leftPacketHandler = new HandlePacket(this, "LEFT");
        rightPacketHandler = new HandlePacket(this, "RIGHT");

        ConnectThread leftThread = new ConnectThread(leftPacketHandler.getHandler(), leftBTDevice, bluetoothAdapter);
        leftThread.start();
        ConnectThread rightThread = new ConnectThread(rightPacketHandler.getHandler(), rightBTDevice, bluetoothAdapter);
        rightThread.start();


        leftChart = findViewById(R.id.leftChart);
        leftRealTime = new RealTimeLineChart(leftChart, this);
        leftRealTime.configureChart();

        rightChart = findViewById(R.id.rightChart);
        rightRealTime = new RealTimeLineChart(rightChart, this);
        rightRealTime.configureChart();
        run();
    }

    public void updateLeftValues(Float[] metrics){
        for(int i = 1; i < 7; i++){
            if(metrics[i] == null){
                return;
            }
        }
        ((TextView)findViewById(R.id.leftStepCountNumber)).setText(Float.toString(metrics[4]));
        ((TextView)findViewById(R.id.leftContactTimeNumber)).setText(Float.toString(metrics[5]));
        ((TextView)findViewById(R.id.leftContactForceNumber)).setText(Float.toString(metrics[6]));
    }

    public void updateRightValues(Float[] metrics){
        for(int i = 1; i < 7; i++){
            if(metrics[i] == null){
                return;
            }
        }
        ((TextView)findViewById(R.id.rightStepCountNumber)).setText(Float.toString(metrics[4]));
        ((TextView)findViewById(R.id.rightContactTimeNumber)).setText(Float.toString(metrics[5]));
        ((TextView)findViewById(R.id.rightContactForceNumber)).setText(Float.toString(metrics[6]));

    }

    public void enableBluetoothAdapter(){
        if(!bluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, BluetoothConstants.REQUEST_ENABLE_BLUETOOTH);
        }
    }

    @Override
    public void updateMetrics(String foot) {
        switch(foot){
            case "LEFT":
                updateLeftValues(leftPacketHandler.getMetrics());
                leftRealTime.addEntry("Yaw", Color.BLUE, leftPacketHandler.getMetrics()[1],0);
                leftRealTime.addEntry("Pitch", Color.GREEN, leftPacketHandler.getMetrics()[2],1);
                leftRealTime.addEntry("Roll", Color.RED, leftPacketHandler.getMetrics()[3],2);
                break;
            case "RIGHT":
                updateRightValues(rightPacketHandler.getMetrics());
                rightRealTime.addEntry("Yaw", Color.BLUE, rightPacketHandler.getMetrics()[1],0);
                rightRealTime.addEntry("Pitch", Color.GREEN, rightPacketHandler.getMetrics()[2],1);
                rightRealTime.addEntry("Roll", Color.RED, rightPacketHandler.getMetrics()[3],2);
                break;
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void run() {
        updateDistanceTravelled();
        root.postDelayed(this, PERIOD);
    }


    public void updateDistanceTravelled(){
        CharSequence heightInput = (((TextView)findViewById(R.id.selectHeight)).getText());
        final float STRIDE_LENGTH_CONSTANT = 0.415F;
        final int cmToM = 100;
        if(heightInput != null && heightInput.length() > 0){
            float height = Float.parseFloat(heightInput.toString());
            float strideLength = (height * STRIDE_LENGTH_CONSTANT) / cmToM ;
            ((TextView)(findViewById(R.id.strideLength))).setText(Float.toString(strideLength));

            if(leftPacketHandler.getMetrics().length < 4 || rightPacketHandler.getMetrics().length < 4){
                return;
            }

            if(leftPacketHandler.getMetrics()[4] == null || rightPacketHandler.getMetrics()[4] == null){
                return;
            }
            ((TextView)(findViewById(R.id.leftDistanceTravelled))).setText(Float.toString((strideLength * leftPacketHandler.getMetrics()[4])));
            ((TextView)(findViewById(R.id.rightDistanceTravelled))).setText(Float.toString(strideLength * rightPacketHandler.getMetrics()[4]));
        }
    }

}
