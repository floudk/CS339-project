import netifaces
from socket import *
from socket import *


s = netifaces.ifaddresses('eth0')[netifaces.AF_INET][0]
board_cast_ip = s['board_cast']
serverSocket = socket(AF_INET, SOCK_DGRAM)

words="Ivan Qing check"
byte_message = bytes(words, "utf-8")
serverSocket.sendto(byte_message, (board_cast_ip,12000))

serverSocket.close()
