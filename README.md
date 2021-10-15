# QQSignBot2

## 前言

此项目的前身为: [QQSignBot](https://github.com/HuPeng333/QQSignBot)

之前的项目采用的是SpringBoot的形式，所有功能集成到了一个jar包下，写到后期，内容十分臃肿难以维护

因此二代将采用**微服务架构**

*当然，要写微服务项目所付出的代价是很多的，因为微服务最终要上线感觉特别麻烦,而且一般人是没有条件去搞集群什么的！希望自己能坚持写下去吧！！！*

## 项目目录

### bot-core

机器人核心，机器人的启动器，用于管理启动的模块

### bot-starter-auto-sign

机器人拓展，用于开启自动打卡

### auto-sign-api

自动打卡功能服务的相关api

### auto-sign-provider

作为服务提供者,提供自动打卡等功能(跟一代一样，不是所有人都可以用的)


## 启动项目

*还没写好呢！*

## 其它

本项目所有踩坑都会记录在Notion上: [传送门](https://zinc-mass-9cc.notion.site/QQSignBot2-fb5cd897cdfb4ca48abee734d46ae04f)