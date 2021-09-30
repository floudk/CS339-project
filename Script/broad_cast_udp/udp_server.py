from socket import *
serverSocket = socket(AF_INET, SOCK_DGRAM)
serverSocket.bind(('', 54545))

while True:
    rand = random.randint(0, 55)
    message, address = serverSocket.recvfrom(1024)
    #messages includes a sequence number and current timestapme
    print(message.decode("utf-8"))
    res_mess="I got it"
    recs = res_mess.decode("utf-8")
    serverSocket.sendto(recs, address)
serverSocket.close()