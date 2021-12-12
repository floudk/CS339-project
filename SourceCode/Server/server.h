#ifndef SERVER_H
#define SERVER_H
#include <stdio.h>
#include <string>
#include <time.h>
#include <unistd.h>
#include <stdlib.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <pthread.h>
#include <bits/stdc++.h>
#include <fstream>
using namespace std;

// #define JOIN
#define DEBUGNOTWITHLOCALDATA
#define DEBUGWITHNETWORK
//#define OLDCHECK
#define numOfStuInOneThread 5
#define numOfThread 20
#define charLength 100
#define checktime 600
#define numOfBuffer 1024
#define recvBufLength 65532
#define teacherPort "12318"
#define studentPort "12313"
#define teacherSignal "./data/teacher.txt"
#define dataSetPath "./data/dataset.txt"
#define name2id "./data/name2id.txt"
//#define newname2id "./data/newname2id.txt"
int contrast(string sz1,string sz2);
string decodeid(string s);
#endif //SERVER_H
