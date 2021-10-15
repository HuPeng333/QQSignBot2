# bot-starter-auto-sign

该模块用于自动打卡功能

## 相关指令
**所有指令都是需要私聊机器人**

- [自动打卡]():通过机器人,获取自动打卡相关帮助
- [注册自动打卡 token 学号 联系电话 家长姓名 家长联系电话]():注册自动打卡服务
- [开启自动打卡]():开启自动打卡,需要先注册
- [关闭自动打卡]():关闭自动打卡,需要先注册
- [修改 [相关数据] [新值]](): 修改相关数据
 
   例如: '[修改 token xxx]()';你也可以修改你打卡的地点,格式: '[修改 打卡地点 月球]()'


## 使用方法
1. 导入依赖
   
   ```xml
   <dependency>
        <groupId>priv.xds</groupId>
        <artifactId>bot-starter-auto-sign</artifactId>
   </dependency>
   ```

2. 在配置文件上添加
    ```properties
    bot.auto-sign.enabled=true
    ```