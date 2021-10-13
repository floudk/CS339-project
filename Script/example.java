private BroadcaseReceiver mReceiver = new BroadcastReceiver(){
    @override

    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equal(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
            List<ScanResult> results = mWifiManager.getScanResults();
            string infos="";
            List wifis = new ArrayList();
            for(ScanResult ScanResult:results){
                if("".equals(scanResult.SSID)){
                    continue;
                }
                int level = WifiManager.calculateSignalLevel(scanResult,level,100);
                String info - scanResult.SSID+":"+level;
                Wifi wifi = new Wifi(scanResult.SSID,level)
                wifis.add(wifi);

                infos+=info+"\n";

            }
            wifiDatas.add(wifis);
            if(wifiDatas.size()%10==0){
                //save
                saveResult();
                String info = "current sample: "+(sampleState==0?"negative ":"positive")+"\n";
                info+="current save data num: "+wifiDatas.size();
                tvSampleInfo.setText(info);
            }
            Log.d("scanWifi".infos);
            tvInfos.setText(infos);

            if(isSample){
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        scanWifi();
                    }
                },1000);
            }
        }
    }
}