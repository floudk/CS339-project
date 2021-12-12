#include "server.h"
pthread_mutex_t rc_mutex;
map <string, string> name;
map <string, string> nametemp;
set <string> idset;
string vague = "";
string check = ""; 
string t="Teacher:\n";
char response[charLength] = "You have checked in.";
char failure[charLength] = "Your check is invalid.";
char cheating[charLength] = "We cannot match your device's identification code, so your check failed.";
char cannotFind[charLength] = "We cannot find your student name, so please check your name.";
char vaguecheck[charLength] = "Your position is vague, please contact with your teacher.";
char repeat[charLength] = "Don't use your device to check other student's name.";

struct thread_recdata{
   int  sockfd;                         //socket
   char *info;                          //接受的数据
   //struct sockaddr_in teacheraddr;    //老师端地址
};

void *recvinfo(void *rec_data)
{
  struct thread_recdata *thread_info;
  thread_info = (struct thread_recdata *)rec_data;
  ssize_t rbytes, sbytes;
  map<string,string>::iterator p;
  socklen_t socklen=sizeof(struct sockaddr_in);                         // struct sockaddr_in的大小
  struct sockaddr_in clientaddr;                                        // 客户端的地址信息。
  
  int j, k=0, nameend, contr, rc;
  bool isidinset, idgeneral;
  string _info, _name, id;

  while (k < numOfStuInOneThread){
    memset(&clientaddr, 0, sizeof(clientaddr));
    rbytes = recvfrom(thread_info->sockfd, thread_info->info, recvBufLength, 0, (struct sockaddr*)&clientaddr, &socklen);
    if(rbytes == -1){perror("recieve student's checking message");continue;}
    cout<<"SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS\n";
    //printf("ip: %s,port: %d, message: %s\n", inet_ntoa(clientaddr.sin_addr), ntohs(clientaddr.sin_port), thread_info->info);

    _info = thread_info -> info ;
    nameend = 0;
    for (auto i:_info){
      if(i ==' ')break;
      _name.push_back(i);
      nameend++;
    }
      
    j = nameend+1;
    while(1){
      id.push_back(thread_info->info[j]);
      if(thread_info->info[j]=='\n')break;
      j++;
    }
    cout<<"before decoding ==  "<<id<<endl;

    thread_info->info[(int)rbytes] = 0;
    contr = contrast(_info, t);
    idgeneral = (id[0]!='@')? true : false;
    cout<<"after decoding id===="<<id<<endl;
    cout<<"contr == "<<contr<<endl;

  #ifndef OLDCHECK
    pthread_mutex_lock(&rc_mutex);
    p=name.find(_name);
    if(p == name.end()){
      cout<<"Can't find student name:"<<_name<<endl;
      sbytes = sendto(thread_info->sockfd, cannotFind, strlen(cannotFind), 0, (const struct sockaddr *)&clientaddr, socklen);
      if(sbytes == -1)perror("sendto cannotFind invalid");
      continue;
    }
    isidinset = (idset.find(id)!=idset.end())? true : false;
    if((!idgeneral) || (idgeneral && p->second==id) || (idgeneral && p->second=="" && !isidinset)){
      if(idgeneral){
        if(p->second == "")p->second = id;
        if(!isidinset)idset.insert(id);
      }
      if(contr == 1){
        check = check + _name + "_1 ";
        cout<<"Student name: "<<_name<<"checked in!"<<endl;
        sbytes = sendto(thread_info->sockfd, response, strlen(response), 0, (const struct sockaddr *)&clientaddr, socklen);
        if(sbytes == -1){perror("sendto response invalid");continue;}else cout<<"send to student response success."<<endl;
      }
      if(contr == 0){
        vague = vague + _name + "_0 ";
        cout<<"Student name: "<<_name<<"'s checking is vague."<<endl;
        sbytes = sendto(thread_info->sockfd, vaguecheck, strlen(vaguecheck), 0, (const struct sockaddr *)&clientaddr, socklen);
        if(sbytes == -1){perror("sendto vaguecheck invalid");continue;}else cout<<"send to student vaguecheck success."<<endl;
      }
      if(contr == -1){
        cout<<"Student name:  "<<_name<<"  fails to check in!"<<endl;
        sbytes = sendto(thread_info->sockfd, failure, strlen(failure), 0, (const struct sockaddr *)&clientaddr, socklen);
        if(sbytes == -1){perror("sendto failure invalid");continue;}else cout<<"send to student failure success"<<endl;
      }
    } else {
      vague = vague + _name + "_0 ";
      if(p->second == ""){
        cout<<"Student name:  "<<_name<<"'s device has been used to check"<<endl;
        sbytes = sendto(thread_info->sockfd, repeat, strlen(repeat), 0, (const struct sockaddr *)&clientaddr, socklen);
        if(sbytes == -1){perror("sendto repeat invalid");continue;}else cout<<"send to student repeat success"<<endl;
      } 
      else if(p->second != id){
        cout<<"Student name:  "<<_name<<" uses different device for checking."<<endl;
        sbytes = sendto(thread_info->sockfd, cheating, strlen(cheating), 0, (const struct sockaddr *)&clientaddr, socklen);
        if(sbytes == -1){perror("sendto cheating invalid");continue;}else cout<<"send to student cheating success"<<endl;
      }
    }
    pthread_mutex_unlock(&rc_mutex);

#else 
  if(contr == 1||contr == 0){    
    pthread_mutex_lock(&rc_mutex);                                                                                //check student's id 
    p=nametemp.find(_name);
    if(p == nametemp.end()){
      cout<<"Can't find student name!\n";
      sbytes = sendto(thread_info->sockfd, cannotFind, strlen(cannotFind), 0, (const struct sockaddr *)&clientaddr, socklen);
      if(sbytes == -1)perror("sendto cannotFind invalid");
      continue;
    }
    if((p->second!="" && p->second!=id)&&id[0]!='@'){                                                             //检查标识码是否匹配
      //cout<<"Stu: "<<Name<<"fail to check in!\n";
      sbytes = sendto(thread_info->sockfd, cheating, strlen(cheating), 0, (const struct sockaddr *)&clientaddr, socklen);
      if(sbytes == -1){perror("sendto cheating invalid");continue;} else cout<<"send to student success.\n";  
      vague = vague + _name + "_0 ";   
    } 
    else {
      if(p->second == ""){
        if(id[0]!='@')name.find(_name)->second = id;
        //nametemp.erase(_name);
        if(contr == 1)check = check + _name + "_1 ";
        if(contr == 0)vague = vague + _name + "_0 ";
      } else {
        //nametemp.erase(_name);
        if(contr == 1)check = check + _name + "_1 ";
        if(contr == 0)vague = vague + _name + "_0 ";
      }
      cout<<"Student number: "<<_name<<"checked in!\n";
      sbytes = sendto(thread_info->sockfd, response, strlen(response), 0, (const struct sockaddr *)&clientaddr, socklen);
      if(sbytes == -1){perror("sendto response valid");continue;}else cout<<"send to student success.\n";
    }
    pthread_mutex_unlock(&rc_mutex);
  }
  else {
    cout<<"Student number:  "<<_name<<"  fail to check in!\n";
    sbytes = sendto(thread_info->sockfd, failure, strlen(failure), 0, (const struct sockaddr *)&clientaddr, socklen);
    if(sbytes == -1){perror("sendto failure invalid");continue;}else cout<<"send to student success.\n";
  }     
  cout<<"Student's check: contr=="<<contr<<'\n';
#endif   
    _info.clear();  
    _name.clear();
    id.clear();
    k++;
  }
}

void *recvTeacherInfo(void *rec_data){
  struct thread_recdata *thread_info;
  thread_info = (struct thread_recdata *)rec_data;
  ssize_t rbytes, sbytes;
  socklen_t socklen=sizeof(struct sockaddr_in);                               // struct sockaddr_in的大小
  struct sockaddr_in clientaddr;                                              // 客户端的地址信息。
  memset(&clientaddr, 0, sizeof(clientaddr));
  char buf[numOfBuffer], temp[numOfBuffer];
  string namelist = "";
  //cout<<"jump in DEBUGTEACHER!\n";
  while(true){
    rbytes = recvfrom(thread_info->sockfd, thread_info->info, recvBufLength, 0, (struct sockaddr*)&clientaddr, &socklen);
    if(rbytes==-1){perror("Monitor recvfrom teacher's message");break;}
    pthread_mutex_lock(&rc_mutex);
    namelist = vague + check;
      
    strncpy(temp,namelist.data(),namelist.size());
    temp[namelist.size()]='\0';
      
    //cout<<"strlen = "<<strlen(temp)<<'\n';
    if(strlen(temp) == 0)temp[0]='#';
    cout<<"temp == "<<temp<<'\n';
    sbytes = sendto(thread_info->sockfd, temp, strlen(temp), 0, (const struct sockaddr *)&clientaddr, socklen);
    //printf("sendtoteacher2 ip: %s,port: %d, message: %s\n", inet_ntoa(clientaddr.sin_addr), ntohs(clientaddr.sin_port), thread_info->info);
    if(sbytes!=-1)cout<<"listening thread reports to teacher successfully.\n";
    pthread_mutex_unlock(&rc_mutex);
  }
}

int main()//int argc, char *argv[])
{
#ifndef DEBUGNOTWITHLOCALDATA                                                  //加载老师
  string pathTeacher = teacherSignal;
  string pathData = dataSetPath;
  ifstream inTeacher(pathTeacher);
  ifstream students(pathData);
  if(!inTeacher.is_open()){perror("open teacher file");return -1;}
  if(!students.is_open()){perror("open students file");return -1;}
  ostringstream teacher;
  teacher<<inTeacher.rdbuf();
  t=teacher.str();                                                         //load student
  string line, stu;
  //string file;
  //bool flag = true;
  int sum = 0, num1 = 0, num0 = 0, num_1 = 0;
  if(students){
    while (getline(students, line)){                                     // line中不包括每行的换行符
      stu += line + "\n";
      /*
      if(!flag){
        file += line + to_string(sum) + '\n';
        flag = true;
      } 
      else file += line +'\n';
      */
      if(line=="......"){
        sum++;
        if(contrast(stu,t)==1){
          num1++;
          cout<<"No."<<sum<<"'s result is 1, and we have "<<num1<<" students checking 1 so far."<<endl;
        }else if(contrast(stu,t)==0){
          num0++;
          cout<<"No."<<sum<<"'s result is 0, and we have "<<num0<<" students checking 0 so far."<<endl;
        }else if(contrast(stu,t)==-1){
          num_1++;
          cout<<"No."<<sum<<"'s result is -1, and we have "<<num_1<<" students checking -1 so far."<<endl;
        }
        stu.clear();
        //flag = false;
      }
    }
  }
  inTeacher.close();
  students.close();
  /*
  remove(dataSetPath);
  ofstream outfile;
  outfile.open(dataSetPath);
  outfile.flush();
  outfile << file;
  outfile.close();
  */
  return 0;
#else
  //if (argc!=2)printf("Using:./server port\nExample:./server 5005\n\n"); return -1;

  int listenfd, listenstd;
  if ( (listenfd = socket(AF_INET,SOCK_DGRAM,0))==-1){perror("socket");return -1;}
  if ( (listenstd = socket(AF_INET,SOCK_DGRAM,0))==-1){perror("socket");return -1;}

  struct sockaddr_in servaddr, stdaddr;
  memset(&servaddr,0,sizeof(servaddr));
  memset(&stdaddr,0,sizeof(stdaddr));
  servaddr.sin_family = AF_INET;
  servaddr.sin_addr.s_addr = htonl(INADDR_ANY);                        // 任意ip地址。
  stdaddr.sin_family = AF_INET;
  stdaddr.sin_addr.s_addr = htonl(INADDR_ANY); 
  //servaddr.sin_addr.s_addr = inet_addr("192.168.76.128");            // 指定ip地址。
  servaddr.sin_port = htons(atoi(teacherPort));                        // 指定通信端口。
  stdaddr.sin_port = htons(atoi(studentPort));

  if (bind(listenfd,(struct sockaddr *)&servaddr,sizeof(servaddr)) != 0 ){perror("bind teacher port");close(listenfd);return -1;}
  if (bind(listenstd,(struct sockaddr *)&stdaddr,sizeof(stdaddr)) != 0 ){perror("bind student port");close(listenstd);return -1;}

  //struct thread_senddata thread_info;                                //发送信息函数利用的信息结构体
  struct thread_recdata thread_rec;
  struct thread_recdata teacher_rec;
  pthread_t thread[numOfThread];                                      //存储线程的id
  pthread_t teacherThread;
  pthread_attr_t attr;                                                //线程属性设置
  int rc, j;                                                          //创建线程的返回值，检查是否创建成功
  void *status;
  char buffer[numOfBuffer];

  string pathStudent = name2id;
  ifstream inStudentName(pathStudent);
    
  string s="", firstname="", id="", tempstring="";
  bool flag = false;
  if(!inStudentName.is_open()){perror("open student name file");return -1;}
  while (inStudentName>>s){
    for(auto elem : s){
      if(elem == ' '){
        firstname = tempstring;
        tempstring.clear();
        flag = true;
      }
      else
        tempstring.push_back(elem);
    }
    if(flag){
      id = tempstring;
      flag = false;
    } else {
      firstname = s;//tempstring;
      id = "";
    }
    name.insert(pair<string, string>(firstname, id));
    tempstring.clear();
    firstname.clear();
    id.clear();
  }
  inStudentName.close();

  //cout<<"map size of name:"<<name.size()<<endl;  
  nametemp = name; 

  thread_rec.sockfd = listenstd;
  thread_rec.info = buffer;
  teacher_rec.sockfd = listenfd;
  teacher_rec.info = buffer;
  pthread_attr_init(&attr);
#ifdef JOIN
  pthread_attr_setdetachstate(&attr,PTHREAD_CREATE_JOINABLE);
#endif
  pthread_setcanceltype(PTHREAD_CANCEL_ASYNCHRONOUS, NULL);

  string namelist = "";
  for(auto &elements : name){
    namelist+=elements.first+" ";
  }
  cout<<"send to teacher:"<<namelist<<'\n';

  char tmp[numOfBuffer];
  strncpy(tmp,namelist.data(),namelist.size()-1);
  tmp[namelist.size()-1]='\0';
  
  ssize_t rbytes, sbytes, tbytes;
  socklen_t socklen=sizeof(struct sockaddr_in);                     // struct sockaddr_in的大小
  struct sockaddr_in clientaddress;                                 // 老师端的地址信息。
  memset(&clientaddress, 0, sizeof(clientaddress));

  char buf[4096];

#ifdef DEBUGWITHNETWORK
  rbytes = recvfrom(teacher_rec.sockfd, buf, sizeof(buf), 0, (struct sockaddr*)&clientaddress, &socklen);

  //cout<<"First recieve content: "<<buf<<'\n';
  sbytes = sendto(teacher_rec.sockfd, tmp , namelist.size()-1, 0, (const struct sockaddr *)&clientaddress, socklen);         
  if(sbytes == -1)cout<<"Send to teacher failed!\n";
  tbytes = recvfrom(teacher_rec.sockfd, buf, sizeof(buf), 0, (struct sockaddr*)&clientaddress, &socklen);                    
  //if(tbytes!= -1)cout<<"Recieve from teacher twice, success!\n";
  if(tbytes == -1)cout<<"Recieve from teacher twice failed!\n";
  //cout<<"Second buffer:"<<buf<<'\n'; 
  t += buf;

#endif

  rc = pthread_create(&teacherThread, &attr ,recvTeacherInfo,(void *)&teacher_rec);
  if(rc){printf("Error:unable to create teacher thread");exit(-1);}
  for(j=0;j<numOfThread;j++){
    rc = pthread_create(&thread[j], &attr ,recvinfo,(void *)&thread_rec);
    if(rc){printf("Error:unable to create student thread");exit(-1);}
  }                                                                         
  /*
  cout<<"Below is the list of students unchecked:"<<'\n';                            //输出未签到名单：
  for(auto &element : nametemp)
  {
    cout<<"Student name:"<<element.first<<endl;
    cout<<"ID:"<<element.second<<'\n';
  }
  */ 
#ifndef JOIN
  sleep(checktime);
  if(pthread_cancel(teacherThread))
    cout<<"Cancel teacher thread failed!"<<endl;
  for(j=0;j<numOfThread;j++){
    if(pthread_cancel(thread[j]))
      cout<<"Cancel student thread["<< j <<"] failed!"<<endl;
  }
#else
  rc = pthread_join(teacherThread, &status);
  if (rc){printf("Error:unable to join teacher");exit(-1);}

  for(int i = 0 ;i < numOfThread;i++){
    rc = pthread_join(thread[i], &status);
    if (rc){
      printf("Error:unable to join student");
      exit(-1);
    }
  }
#endif

  remove(name2id);
  string content = "";
  ofstream out;
  out.open(name2id);
  for(auto &elements : name){
    content += elements.first;
    if(elements.second==""){
      content += '\n';
    }else{
      content += " "; 
      content += elements.second;
      content += '\n';
    }
  }
  out.flush();
  out << content;
  out.close();
  pthread_mutex_destroy(&rc_mutex); 
  pthread_attr_destroy(&attr);
  pthread_exit(NULL);                                                            //删除线程
  close(listenfd);
  close(listenstd);
  return 0;
#endif
}