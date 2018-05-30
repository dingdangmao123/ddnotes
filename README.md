# 叮当记账

一个极其简洁的记账App, UI风格简单清爽，支持基本记账功能


## 主要特性

- 消费分类完全自定义，并且支持删除功能，自由定制；
- 可按照年/月/日/标签分类检索你的账单，并提供图表式分析。
- 支持记录按时间一键导出账单功能。
- 支持密码锁功能

## 技术分析

- 采用SQLite数据库存储数据，主要包含两张表，一张是存储消费记录，另一张储存消费类别
- 采用EventBus传递通知消息，例如添加消费记录时，添加新类别后刷新标签


## 运行效果
![](https://github.com/dingdangmao123/WeTouch/blob/master/demo/1.jpg)
![](https://github.com/dingdangmao123/WeTouch/blob/master/demo/2.jpg)


