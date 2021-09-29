# Import modules
import subprocess
import ipaddress
from posix import ST_RELATIME
import netifaces
from netaddr import IPAddress
from tcping import Ping
import os



# Get all hosts on that network
s = netifaces.ifaddresses('eth0')[netifaces.AF_INET][0]
net_addr = s['addr']+'/'+ str(IPAddress(s['netmask']).netmask_bits())
ip_net = ipaddress.ip_network(net_addr,False)
all_hosts = list(ip_net.hosts())


for i in range(len(all_hosts)):
    res = subprocess.call(['ping', '-c', '1', str(all_hosts[i])],stdout=subprocess.DEVNULL,
    stderr=subprocess.STDOUT)
    if res==2:   
        print(str(all_hosts[i]), "is Offline")
    elif res==0:
        print(str(all_hosts[i]), "is Online")
    else:
        print(str(all_hosts[i]), "is Offline")
