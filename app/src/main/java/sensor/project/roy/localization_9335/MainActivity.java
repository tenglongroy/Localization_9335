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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonlocate;
    private TextView text1;
    private ImageView iv_canvas;
    private CanvasLocation mcanvasLocation;
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
        //buttonlocate.setBackgroundResource(R.drawable.button_green);
        buttonlocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiManager.startScan();
                appScan = true;     //prevent non-stop System-auto scan
                //buttonlocate.setVisibility(View.INVISIBLE);
                buttonlocate.setBackgroundResource(R.drawable.shape_gray);
                buttonlocate.setClickable(false);
            }
        });
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
                    text1.setText("Retried 10 times and failed, stop");
                    buttonlocate.setBackgroundResource(R.drawable.shape_green);
                    buttonlocate.setClickable(true);
                    return;
                }
                text1.setText("Not enough AP, retry "+tryTimes+" times");
                if(tryTimes == 1){
                    mcanvasLocation.resumeCanvas();
                    mcanvasLocation.clearSCR();
                }
                mWifiManager.startScan();
                tryTimes++;
                appScan = true;     //allow scanResult accept
            }
            else {
                tryTimes = 1;
                float[] receivedLocation = msg.getData().getFloatArray("location");
                text1.setText("Location\nX: "+receivedLocation[0]+" Y: "+receivedLocation[1]+" floor: "+receivedLocation[2]);
                mcanvasLocation.drawLocation(receivedLocation[0],receivedLocation[1]);
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
                        float[] location = Grocery.computeLocation(result);
                        if(location == null){   //not enough AP to localize
                            Message msg = new Message();
                            msg.what = 0;
                            myTextViewHandler.sendMessage(msg);
                        }
                        else{
                            Message msg = new Message();
                            msg.what = 1;
                            Bundle bd = new Bundle();
                            bd.putFloatArray("location", location);
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