# CS339-project



|      | 需求            | 现存问题       | 备注                 |
| ---- | --------------- | -------------- | -------------------- |
| 前端 | 安卓：搜集+发送 | 教师端需要处理 | 硬编码端口**12212**  |
| 后端 | 接受+数据处理   |                | 必须在**12212**上接受TCP |
| 其它 | PPT             |                |                      |

### 后端

创建了一个阿里云的云服务器，可以用ssh登录

```bash
ssh foo@106.14.95.162
```

106.14.95.162是公网IP，foo用户密码为`ilovecs`

服务器允许常用协议，以及

- TCP支持 12200--12299
- UDP支持 12300--12399

如有其他需要，联系@floudk 使用管理员账号登录阿里云进行修改

### 前端

后面会将android上传到github，协同开发
