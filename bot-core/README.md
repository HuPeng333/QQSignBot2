# bot-core

此模块为机器人的核心启动模块,**本模块不包含任何功能**

但是你可以导入其它模块来引入别的功能! *详细请见 :* [导入其它模块](#导入其它模块)

## 启动

下面提供两种方式启动,不管你使用哪种方式,你都需要提供配置文件,具体 *(yaml)* 格式如下
```yaml
    # 项目完成后提供,非生产模式可以直接克隆项目后使用
```

这是一个 **标准的SpringBoot项目** ,所以你可以安装需求,将配置文件放到相应的位置

### 直接下载jar包启动
你可以直接从RELEASE列表中下载jar包,但jar包中将会 **默认引入所有的额外模块!** 所以体积会比较大

### 克隆本项目后,选择所需依赖后启动

*详细请见 :* [导入其它模块](#导入其它模块)

建议在打包时跳过测试!
```shell
    mvn package -D maven.skip.test=true
```

## 导入其它模块

在项目中,所有以bot-starter开头的模块,都是能够为机器人提供额外功能的依赖

比如你想使用bot-starter-auto-sign的功能,首先在pom.xml中添加依赖
```xml
    <dependency>
        <groupId>priv.xds</groupId>
        <artifactId>bot-starter-auto-sign</artifactId>
    </dependency>
```
然后在配置文件(properties示例)中添加:

```properties
    bot.auto-sign.enabled=true
```

默认为关闭状态,在开启后即可使用!

*相关配置可以查看相关依赖的README.md*