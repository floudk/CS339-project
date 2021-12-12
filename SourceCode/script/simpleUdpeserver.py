import  socket
import time
i=0
while 1:
    udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # 2. 绑定一个本地信息
    localaddr = ("",12320)
    udp_socket.bind(localaddr)

    # 3. 接收数据
    recv_data = udp_socket.recvfrom(1024)

    recv_msg = recv_data[0]
    send_addr = recv_data[1]
    localtime = time.asctime(time.localtime(time.time()))
    print("["+localtime+"]receiving..."+str(send_addr))
    print(recv_msg.decode("utf-8"),flush=True)
    
    # 4. 打印接收到的信息
    # print("%s:%s" % str(send_addr), recv_msg.decode("utf-8"))
    udp_socket.sendto(("got it "+str(i)).encode("utf-8"),send_addr)
    # 5. 关闭套接字
    udp_socket.close()
    i+=1
