# CS339-project
A teamwork project for course Computer Network
# State
老师的意思基本认可项目，但可能在mDNS之外有更好的做法
# To do
- [ ] 查找更好的做法
    - ~~轮询访问同一网段里所有IP地址~~:尝试后发现两个问题，1.很慢，如果真要做必须要保证极高的平行度，2.似乎ping不通，在图书馆尝试ping了很多，只有两个地址能ping通，查资料发现很多计算机都默认不开启ICMP协议
    - client直接发送组播报文实现
- [ ] 了解微信小程序开发与Web开发
- [ ] 了解mDNS协议以及可能的替代方案
- [ ] 实现从Win2Win的check-in
