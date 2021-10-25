#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
 
int main(int argc,char *argv[])
{
  if (argc!=2)
  {
    printf("Using:./server port\nExample:./server 5005\n\n"); return -1;
  }

  int listenfd;
  if ( (listenfd = socket(AF_INET,SOCK_STREAM,0))==-1)
  { 
    perror("socket"); 
    return -1; 
  }
  
  struct sockaddr_in servaddr;    
  memset(&servaddr,0,sizeof(servaddr));
  servaddr.sin_family = AF_INET;  
  servaddr.sin_addr.s_addr = htonl(INADDR_ANY);          // 任意ip地址。
  //servaddr.sin_addr.s_addr = inet_addr("192.168.31.10"); // 指定ip地址。
  servaddr.sin_port = htons(atoi(argv[1]));  // 指定通信端口。

  if (bind(listenfd,(struct sockaddr *)&servaddr,sizeof(servaddr)) != 0 )
  { 
    perror("bind"); 
    close(listenfd); 
    return -1; 
  }
 
  // 把socket设置为监听模式。
  if (listen(listenfd,5) != 0 )
  { 
    perror("listen"); 
    close(listenfd); 
    return -1; 
  }
 
  //接受客户端的连接。
  int  clientfd;                  // 客户端的socket。
  int  socklen=sizeof(struct sockaddr_in); // struct sockaddr_in的大小
  struct sockaddr_in clientaddr;  // 客户端的地址信息。
  clientfd=accept(listenfd,(struct sockaddr *)&clientaddr,(socklen_t*)&socklen);
  printf("Client (%s) connected.\n",inet_ntoa(clientaddr.sin_addr));
 
  //与客户端通信，接收客户端发过来的报文后，回复ok。
  char buffer[1024];
  while (1)
  {
    int iret;
    memset(buffer,0,sizeof(buffer));
    if ( (iret=recv(clientfd,buffer,sizeof(buffer),0))<=0) // 接收客户端的请求报文。
    {
       printf("iret=%d\n",iret); break;   
    }
    printf("Recieve:%s\n",buffer);
 
    strcpy(buffer,"ok");
    if ( (iret=send(clientfd,buffer,strlen(buffer),0))<=0) // 向客户端发送响应结果。
    { 
      perror("send"); 
      break;
    }
    printf("Send:%s\n",buffer);
  }
 
  //关闭socket，释放资源。
  close(listenfd); close(clientfd); 
}
