package yfqing.cs339.pa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import yfqing.cs339.pa.encode.encode;
import yfqing.cs339.pa.wifi.WifiStream;

public class CheckFragment<uiHandler> extends Fragment {
    String wifi_name;
    int wifi_rssi;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private WifiManager wifiManager;
    //自定义一个数据类型，后面用于存储与排序
    private EditText port_view;
    List<String> WifiList = new ArrayList<>();
    private boolean _unsend = true;
    public CheckFragment() {
    }
    public static CheckFragment newInstance(String param1, String param2) {
        CheckFragment fragment = new CheckFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check, container, false);
        Button btn = rootView.findViewById(R.id.check_btn);
        EditText my_input = rootView.findViewById(R.id.my_input);
        port_view =  rootView.findViewById(R.id.PORT);
        port_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == port_view.getId())
                {
                    port_view.setCursorVisible(true);
                }
            }
        });
        my_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == my_input.getId())
                {
                    my_input.setCursorVisible(true);
                }
            }
        });
        port_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               port_view.setCursorVisible(false);
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(port_view.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
           }
        }
        );
        my_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                my_input.setCursorVisible(false);
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(my_input.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        }
        );
        port_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(port_view.hasFocus()){
                    port_view.setCursorVisible(true);
                }else{
                    port_view.setCursorVisible(false);
                }
            }
        });
        my_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(my_input.hasFocus()){
                    my_input.setCursorVisible(true);
                }else{
                    my_input.setCursorVisible(false);
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Check in now......", Toast.LENGTH_SHORT).show();
                wifiManager = (WifiManager) v.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);//获取wifi服务
                assert wifiManager != null;
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                wifi_name = wifiInfo.getSSID();
                int networkID = wifiInfo.getNetworkId();
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
                    return;
                }

                List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
                for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                    if (wifiConfiguration.networkId == networkID) {
                        wifi_name = wifiConfiguration.SSID;
                        break;
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                        v.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
                }
                Log.e("input",my_input.getText().toString());

                if(my_input.getText().toString().equals("")){
                    Toast.makeText(getContext(), "请输入姓名", Toast.LENGTH_SHORT).show();
                }else {
                    getWifiList(v,my_input.getText().toString());//调用上面函数获取wifi列表
                }


            }

        });
        rootView.findViewById(R.id.to_teacher).setOnLongClickListener(
                new View.OnLongClickListener(){
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),TeacherActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        return false;
                    }

                });

        return rootView;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getWifiList(View v,String extra_info) {
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
        //然后将list转为string放入WifiList中
        String unique_id;
        try {
            unique_id= getDeviceId(context);
        }catch (Exception e){
            unique_id = "@";
        }
        Log.e("encode",unique_id);
        Log.d("encode",unique_id);
        String tmp_send = extra_info+" "+unique_id+ "\n";
//        String tmp_send="";
        for (int i = 0; i < list.size(); i++) {
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
        int port_number= 12313;
        //Toast.makeText(v.getContext(), "I get"+Integer.toString(list.size()), Toast.LENGTH_SHORT).show();

        try {
            Udp sender = new Udp(tmp_send);
            sender.set_port(port_number);
            Toast.makeText(v.getContext(), "签到中", Toast.LENGTH_SHORT).show();
            sender.start();

        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Connect ERROR", Toast.LENGTH_SHORT).show();
        }

        if (_unsend){
            //try again and again
            try {
                Udp sender = new Udp(tmp_send);
//                sender.set_port(port_number);
//                Toast.makeText(v.getContext(), "签到中", Toast.LENGTH_SHORT).show();
                sender.start();
            } catch (Exception e) {
                Toast.makeText(v.getContext(), "Connect ERROR", Toast.LENGTH_SHORT).show();
            }
        }
        if (_unsend){
            //try again and again
            try {
                Udp sender = new Udp(tmp_send);
//                sender.set_port(port_number);
//                Toast.makeText(v.getContext(), "签到中", Toast.LENGTH_SHORT).show();
                sender.start();
            } catch (Exception e) {
                Toast.makeText(v.getContext(), "Connect ERROR", Toast.LENGTH_SHORT).show();
            }
        }
        if (_unsend){
            //try again and again
            try {
                Udp sender = new Udp(tmp_send);
//                sender.set_port(port_number);
//                Toast.makeText(v.getContext(), "签到中", Toast.LENGTH_SHORT).show();
                sender.start();
            } catch (Exception e) {
                Toast.makeText(v.getContext(), "Connect ERROR", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private class Udp extends Thread {
        private String dataString;

        public Udp(String dataString) {
            this.dataString = dataString;
        }
        private int port_id=12320;
        public void set_port(int port){
            port_id=port;
        }
        public void run() {

            try {
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress ipAddr = InetAddress.getByName("106.14.95.162");
                byte[] sendData = dataString.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sendData.length, ipAddr, port_id);
                clientSocket.send(sendPacket);

                byte[] lMsg = new byte[1000];
                DatagramPacket dp = new DatagramPacket(lMsg, lMsg.length);
                clientSocket.receive(dp);
                String rev =new String(lMsg, 0, dp.getLength());
                _unsend=false;
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), rev, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDeviceId(Context context) throws Exception {
        MyUUID id = new MyUUID();
        String res=id.createGUID(context);

        res =  encode.eenc(res);


        return res;
    }

}