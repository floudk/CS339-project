# 基于Wifi Fingerprint的防作弊签到系统

 组员： [AbigailWilliams1](https://github.com/AbigailWilliams1)   [Electronlxr](https://github.com/Electronlxr)  [floudk](https://github.com/floudk)

## 文件组成说明

### 安卓前端简要说明

安卓工程代码提交了整体的文件包，导入`Android Studio`可直接打开，为了演示和代码复用，我们将教师端和学生端集成在了一个APP内，但彼此没有联系，可以快速解耦为两个APP工程。

因为大部分代码是安卓框架代码，这里仅对于我们主要写的文件及相关文件夹进行说明：

```markdown
cd .\SourceCode\Android\CheckIn\app\src\main
.
├── AndroidManifest.xml   //安卓配置文件，主要是对去权限和工程主要信息进行配置
├── java
│   └── yfqing
│       └── cs339
│           └── pa
|		  ├── MainActivity.java         	 //安卓入口，这里主要是学生端和教室端的入口跳转，无其它作用
│               ├── CheckFragment.java     	       //学生端主要代码文件文件，主要负责学生端的相关信息收集和网络传输
│               ├── Classmates.java             	//教师端使用的学生类数据结构，主要是和服务器交互后进行信息更新
│               ├── GlobalClass.java                     //学生类数据结构的再次封装，使其成为一个静态的全局变量，以便调用
|		             ├── CheckedFragment.java           //教师端已签到学生页面，显示已签到学生数据
│               ├── MaycheckedFragment.java     //教师端签到存疑学生页面，显示签到存疑学生数据
│               ├── UncheckedFragment.java        //教师端未签到学生页面，显示未签到学生数据
│               ├── MyUUID.java                             //设备唯一标识码获取相关文件
│               ├── SPUtil.java                                  //设备唯一标识码持久化存储相关文件
│               ├── TeacherActivity.java           		//教师端主要文件，负责到三个子Fragment的跳转等
│               ├── encode                    			
│               │   └── encode.java				//设备码按照时间加密函数
│               └── wifi
│                   └── WifiStream.java				//wifi数据结构，用以读取和组织
└── res                                  				//安卓UI等相关资源文件，主要是显示界面的配置
    ├── drawable
    │   ├── background.xml
    │   ├── btn_check.xml
    │   ├── btn_input.xml
    │   └── ic_launcher_background.xml
    ├── drawable-v24
    │   └── ic_launcher_foreground.xml
    ├── layout
    │   ├── activity_main.xml
    │   ├── activity_teacher.xml
    │   ├── fragment_check.xml
    │   ├── fragment_checked.xml
    │   ├── fragment_maychecked.xml
    │   └── fragment_unchecked.xml
    ├── mipmap-anydpi-v26
    │   ├── ic_launcher.xml
    │   └── ic_launcher_round.xml
    ├── mipmap-hdpi
    │   ├── ic_launcher.webp
    │   └── ic_launcher_round.webp
    ├── mipmap-mdpi
    │   ├── ic_launcher.webp
    │   └── ic_launcher_round.webp
    ├── mipmap-xhdpi
    │   ├── ic_launcher.webp
    │   └── ic_launcher_round.webp
    ├── mipmap-xxhdpi
    │   ├── ic_launcher.webp
    │   └── ic_launcher_round.webp
    ├── mipmap-xxxhdpi
    │   ├── ic_launcher.webp
    │   └── ic_launcher_round.webp
    ├── values
    │   ├── colors.xml
    │   ├── strings.xml
    │   └── themes.xml
    └── values-night
        └── themes.xml
```

### 服务器后端简要说明

```markdown
├── Android  // 安卓项目文件夹，上一模块已说明
├── Server     //  后端文件夹
│   ├── Makefile   //使用命令make或make all编译程序，成功时输出Done. 使用命令clean清空执行文件，成功时输出Clean.
│   ├── contrast.cpp  //比较函数，比较两个wifi列表的相似度从而判断两个地点的距离远近。
│   ├── data 		//本地数据文件夹
│   │   ├── dataset.txt  	 //在本地测试时使用的学生wifi列表数据集
│   │   ├── name2id.txt		//本地存储的学生及其标识码名单
│   │   └── teacher.txt		//在本地测试时使用的教室wifi列表数据集
│   ├── decode.cpp		//解密函数，解密标识码
│   ├── server.cpp		//服务器主程序
│   └── server.h		//服务器头文件，其中部分宏定义作用如下：
└── script			//测试用脚本文件，对项目本身无作用
    └── simpleUdpeserver.py   //python实现的简易server用来测试输入
```

#### 宏定义说明

这里对代码中部分宏定义进行说明:

```c++
#define DEBUGNOTWITHLOCALDATA	//当该宏未被定义时处于本地测试模式，服务器从本地读取学生与教师的wifi列表测试集
//其他无标记的宏均为程序测试所用,对项目本身无其它意义，这里做保留
#define teacherPort "12318"			//教师端口号
#define studentPort "12313"			//学生端口号
```

## 其它文件说明

```bash
├── PPT      // 安卓项目文件夹，上一模块已说明
│   ├── QuickIn.pptx    //演示用幻灯片
├── SourceCode     //  后端文件夹
├── 报告  
│   ├── report.docx   //word文档
|   ├── report.pdf     //PDF文档
└── app-debug.apk   //Android apk生成文件
```

## 其他说明

安卓App需要对应权限，在安装测试使用时，可能需要手动确认定位权限和网络权限。

<!-- 项目以打包上传[Github]()

**感谢老师和助教的关心和支持，对代码和文件有任何疑问，可以随时联系我们。** -->
