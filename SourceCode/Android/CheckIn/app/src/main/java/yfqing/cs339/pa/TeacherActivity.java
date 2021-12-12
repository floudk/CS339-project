package yfqing.cs339.pa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import yfqing.cs339.pa.wifi.WifiStream;

public class TeacherActivity extends AppCompatActivity {
    private View checked,unchecked,maychecked;
    private FragmentManager fragmentManager;
    private CheckedFragment checkedFragment;
    private UncheckedFragment uncheckedFragment;
    private MaycheckedFragment maycheckedFragment;
    private Button start_btn;
    private Boolean initial_finish = false;
//    private String debug_string = "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15"; //for debug

    private List<String> WifiList = new ArrayList<>();
    private Handler handler = new Handler();

    private int teacher_port=12318;
    private Boolean global_rec_udp = true;
    private int currentTab;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
//        Toast.makeText(TeacherActivity.this,"you get it", Toast.LENGTH_SHORT).show();
        initViews();


        fragmentManager = getFragmentManager();
        setTabSelection(1);
        start_btn=findViewById(R.id.start_checkin);



        start_btn.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "开始签到", Toast.LENGTH_SHORT).show();
                //发送udp报文给后端Teacher
                String tmp_send="10";
                GlobalClass.students = new Classmates();
//                GlobalClass.students.initial(debug_string);
                try {
                    udp2 sender = new udp2(tmp_send);
                    sender.set_port(teacher_port);
                    sender.start();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Connect ERROR", Toast.LENGTH_SHORT).show();
                }

                //接受同学列表
                handler.post(new Runnable() {
                    public void run(){
                        start_btn.setText("签到中...");
                        start_btn.setClickable(false);
                    }
                });

                //开始监听端口接受签到信息
                String sendMes = getWifiList(v);
                Log.e("send",sendMes);
//                String sendMes = "57c300 -40\n" +
//                        "57c301 -40\n" +
//                        "57c302 -40\n" +
//                        "573b60 -45\n" +
//                        "573b61 -45\n" +
//                        "573b62 -45\n" +
//                        "573b63 -45\n" +
//                        "57c310 -52\n" +
//                        "57c311 -52\n" +
//                        "57c312 -52\n" +
//                        "573b70 -56\n" +
//                        "573b71 -56\n" +
//                        "573b72 -56\n" +
//                        "5acb60 -63\n" +
//                        "5acb61 -63\n" +
//                        "5acb63 -63\n" +
//                        "5739c0 -70\n" +
//                        "5739c1 -71\n" +
//                        "5739c2 -71\n" +
//                        "5739c3 -71\n" +
//                        "5737c1 -72\n" +
//                        "5737c0 -73\n" +
//                        "5737c2 -73\n" +
//                        "5737c3 -73\n" +
//                        "5ad720 -74\n" +
//                        "5ad721 -75\n" +
//                        "5ad722 -75\n" +
//                        "5ad723 -75\n" +
//                        "5adb21 -77\n" +
//                        "5adb22 -77\n" +
//                        "5adb23 -77\n" +
//                        "5acb30 -79\n" +
//                        "5acb31 -79\n" +
//                        "5acb32 -79\n" +
//                        "5738d2 -84\n" +
//                        "......";

                try {

                    udp2 sender = new udp2(sendMes);
                    sender.set_port(teacher_port);
                    sender.reveiveable=0;
                    sender.start();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Connect ERROR", Toast.LENGTH_SHORT).show();
                }

                Thread server = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                while(!initial_finish){}

                                while (true) {
                                    try {
                                        udp2 query = new udp2("query");
                                        while (!global_rec_udp){}
                                        global_rec_udp = false;
                                        Thread.sleep(4000);
                                        query.reveiveable=1;
                                        query.start();

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                );
                server.start();
                return false;
            }
        });



    }


    private void initViews() {

        checked = findViewById(R.id.checked_layout);
        unchecked = findViewById(R.id.unchecked_layout);
        maychecked = findViewById(R.id.maychecked_layout);
        checked.setOnClickListener(this::onClick);
        unchecked.setOnClickListener(this::onClick);
        maychecked.setOnClickListener(this::onClick);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update_tabs(){

        if (checkedFragment!=null&&checkedFragment.isVisible()){
            setTabSelection(1);
            setTabSelection(2);
            setTabSelection(0);
        }else if(uncheckedFragment!=null&&uncheckedFragment.isVisible()){
            setTabSelection(2);
            setTabSelection(0);
            setTabSelection(1);
        }else if(maycheckedFragment!=null&&maycheckedFragment.isVisible()){
            setTabSelection(0);
            setTabSelection(1);
            setTabSelection(2);
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checked_layout:
                // 当点击了消息tab时，选中第1个tab
//                Toast.makeText(v.getContext(), "0", Toast.LENGTH_SHORT).show();
                setTabSelection(0);
                break;
            case R.id.unchecked_layout:
                // 当点击了联系人tab时，选中第2个tab
//                Toast.makeText(v.getContext(), "1", Toast.LENGTH_SHORT).show();
                setTabSelection(1);
                break;
            case R.id.maychecked_layout:
                // 当点击了动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            default:
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (checkedFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    checkedFragment = new CheckedFragment();
                    transaction.add(R.id.content, checkedFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.remove(checkedFragment);
                    checkedFragment = new CheckedFragment();
                    transaction.add(R.id.content, checkedFragment);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
//                unCheckedLayout.setBackgroundColor(0xff0000ff);
                if (uncheckedFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.remove(uncheckedFragment);
                }
                uncheckedFragment = new UncheckedFragment();
                transaction.add(R.id.content, uncheckedFragment);
                break;
            case 2:
                // 当点击了动态tab时，改变控件的图片和文字颜色
//                maychecked.setBackgroundColor(0xff0000ff);
                if (maycheckedFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.remove(maycheckedFragment);
                }
                maycheckedFragment = new MaycheckedFragment();
                transaction.add(R.id.content, maycheckedFragment);
                break;
            default:
                // 当点击了设置tab时，改变控件的图片和文字颜色
                break;
        }
        transaction.commit();
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     * 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (checkedFragment != null) {
            transaction.hide(checkedFragment);
        }
        if (uncheckedFragment != null) {
            transaction.hide(uncheckedFragment);
        }
        if (maycheckedFragment != null) {
            transaction.hide(maycheckedFragment);
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        checked.setBackgroundColor(0xffffffff);
        unchecked.setBackgroundColor(0xffffffff);
        maychecked.setBackgroundColor(0xffffffff);
    }


    private class udp2 extends Thread {
        private String dataString;

        public udp2(String dataString) {
            this.dataString = dataString;
        }
        private int port_id=12318;
        public void set_port(int port){
            port_id=port;
        }
        public int reveiveable;
        public void run() {
            try {
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress ipAddr = InetAddress.getByName("106.14.95.162");
                byte[] sendData = dataString.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sendData.length, ipAddr, port_id);
                Log.e("SEND",dataString);
                clientSocket.send(sendPacket);
                if(reveiveable==0){ //initial classmates list
                    byte[] lMsg = new byte[1000];
                    DatagramPacket dp = new DatagramPacket(lMsg, lMsg.length);
                    clientSocket.receive(dp);
                    String rev =new String(lMsg, 0, dp.getLength());
                    handler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void run() {
                            Log.e("REC[0]:",rev);
                            GlobalClass.students.initial(rev);
                            update_tabs();
//                        Toast.makeText(getApplicationContext(), rev, Toast.LENGTH_SHORT).show();
                        }
                    });
                    initial_finish = true;
                }else if (reveiveable==1){ //update students query
                    byte[] lMsg = new byte[1000];
                    DatagramPacket dp = new DatagramPacket(lMsg, lMsg.length);
                    clientSocket.receive(dp);
                    String rev =new String(lMsg, 0, dp.getLength());
                    Log.e("REC[1]:",rev);
                    handler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void run() {
                        GlobalClass.students.update_list(rev);
                        update_tabs();
//                        Toast.makeText(getApplicationContext(), rev, Toast.LENGTH_SHORT).show();
                            global_rec_udp=true;
                        }
                    });

                }

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getWifiList(View v) {
        //定义一个WifiManager对象
        Context context = v.getContext();
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);

        List<ScanResult> scanWifiList = wifiManager.getScanResults();
        List<ScanResult> wifiList = new ArrayList<>();//最终返回的列表


        List<WifiStream> list = new ArrayList<WifiStream>();

        if (scanWifiList != null && scanWifiList.size() > 0) {

            for (int i = 0; i < scanWifiList.size(); i++) {
                ScanResult scanResult = scanWifiList.get(i);
                if (!scanResult.SSID.isEmpty()) {//wifi名称不是空的话
                    String key = scanResult.SSID + " " + scanResult.BSSID + " " + scanResult.level;//显示wifi的名称以及其RSSI
                    list.add(new WifiStream(scanResult.SSID, scanResult.level, scanResult.BSSID));

                }
            }
        }

        String tmp_send="";
        for (int i = 0; i < list.size(); i++) {
//
//            String key = list.get(i).WifiStream_bssid + " " + list.get(i).WifiStream_name + " " + list.get(i).WifiStream_rssi;
            String key = list.get(i).WifiStream_bssid ;
            key = key.substring(8);
            key= key.replace(":","");
            key+= " " + list.get(i).WifiStream_rssi;
            Log.d("output",key);
            WifiList.add(key);//输出key（wifi名称+RSSI）
            tmp_send = tmp_send + key + "\n";
        }
        tmp_send +="......";
        return tmp_send;
    }



}
