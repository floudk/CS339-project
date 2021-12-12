package yfqing.cs339.pa.wifi;

public class WifiStream {
    public String WifiStream_name;
    public int WifiStream_rssi;
    public String WifiStream_bssid;

    public WifiStream(String ssid, int level, String bssid) {
        this.WifiStream_name = ssid;
        this.WifiStream_bssid = bssid;
        this.WifiStream_rssi = level;
    }
}