package sensor.project.roy.localization_9335;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonlocate;
    private TextView text1;
    private ImageView iv_canvas;
    private CanvasLocation mcanvasLocation;
    private CheckBox checkBox;
    WifiManager mWifiManager;
    wifiScanReceiver mReceiver;
    int tryTimes = 1;
    boolean appScan = false;

    String fileIdle = "dataIdle";
    String fileBusy = "dataBusy";
    String fileEmpty = "dataEmpty";



    @Override
    protected void onResume() {
        mReceiver = new wifiScanReceiver();
        registerReceiver(mReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.text1);
        iv_canvas = (ImageView) findViewById(R.id.iv_canvas);
        //iv_canvas.setImageResource(R.drawable.ainsworthandroid);
        iv_canvas.setBackgroundResource(R.drawable.ainsworthandroid);
        mcanvasLocation = new CanvasLocation(this, iv_canvas);
        buttonlocate = (Button) findViewById(R.id.buttonlocate);
        buttonlocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appScan = true;     //prevent non-stop System-auto scan
                mWifiManager.startScan();
                buttonlocate.setBackgroundResource(R.drawable.shape_gray);
                buttonlocate.setClickable(false);
                text1.setText("Scanning for Location");
            }
        });
        checkBox = (CheckBox) findViewById(R.id.chechBox);
        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        mWifiManager.setWifiEnabled(true);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    Handler myTextViewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){      //not enough AP to localize
                if(tryTimes > 10) {
                    tryTimes = 1;
                    text1.setText("Failed 10 times\nStop");
                    buttonlocate.setBackgroundResource(R.drawable.shape_green);
                    buttonlocate.setClickable(true);
                    return;
                }
                text1.setText("Failed\nRetry " + tryTimes + " times");
                if(tryTimes == 1){
                    mcanvasLocation.resumeCanvas();
                    mcanvasLocation.clearSCR();
                }
                System.out.println(tryTimes);
                appScan = true;     //allow scanResult accept
                mWifiManager.startScan();
                tryTimes++;
            }
            else {
                tryTimes = 1;
                String[] receivedLocation = msg.getData().getStringArray("location");
                text1.setText("X: " + receivedLocation[0] + " Y: " + receivedLocation[1] +
                        " floor: " + receivedLocation[2]);
                mcanvasLocation.drawLocation(Float.parseFloat(receivedLocation[0]),
                        Float.parseFloat(receivedLocation[1]));
                //buttonlocate.setVisibility(View.VISIBLE);
                buttonlocate.setBackgroundResource(R.drawable.shape_green);
                buttonlocate.setClickable(true);
                super.handleMessage(msg);
            }
        }
    };
    class wifiScanReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(appScan && intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                Thread locateThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        appScan = false;
                        ArrayList<ScanResult> result = (ArrayList<ScanResult>)mWifiManager.getScanResults();
                        String[] location;
                        if(checkBox.isChecked()){
                            location = Grocery.computeLocation(result, 1);
                        }
                        else {
                            location = Grocery.computeLocation(result, 0);
                        }
                        if(location == null){   //not enough AP to localize
                            Message msg = new Message();
                            msg.what = 0;
                            myTextViewHandler.sendMessage(msg);
                        }
                        else{
                            Message msg = new Message();
                            msg.what = 1;
                            Bundle bd = new Bundle();
                            bd.putStringArray("location", location);
                            msg.setData(bd);
                            myTextViewHandler.sendMessage(msg);
                        }
                        //text1.setText("Currently at:\nX: "+location[0]+" Y: "+location[1]+" Floor: "+location[2]);
                    }
                });
                locateThread.start();
            }
        }
    }

}
