# 极简闹钟/SpecialAlarmClock

- 自制的Android闹钟
- 简单设置周期.
- 点击XX后提前停止闹钟,并且记录停止时间

## 开发日程:

任务          | 进度          |  完成时间
------------- | ------------- | -------------
移植界面  | √  | 2015-11-17
重构添加页面  | √ |2015-11-18
实现添加闹钟的功能|√ |2015-11-19 |
返回时保存数据 |√ |2015-11-19 |
显示闹钟列表| √|2015-11-19 |
移植删除/激活 |√ |2015-11-19 |
编辑闹钟|√ |2015-11-19 |
调整取消闹钟的界面| √|2015-11-27 |
设置取消闹钟的提示| | |
调整取消闹钟的功能| | |
提前取消闹钟| | |
设置闹钟的重复| | |
测试各种情况（重启，锁屏）下闹钟的表现| | |
设置| | |
| | |
| | |


## todo:
1. 定制开发，实现例外，
1. 记录起床时间，
1. 同步起床记录

## improve：

1. 系统静态广播,点击广播提前取消闹钟。该静态广播只在凌晨5点后出现
1. 假期自动跳过;
1. 选取铃声作为闹钟声音
1. 改进闹钟的取消方式

## 参考项目：

http://stackoverflow.com/questions/7594637/alarm-clock-application-source-code

https://github.com/SheldonNeilson/Android-Alarm-Clock

https://github.com/yuriykulikov/AlarmClock

https://github.com/bedditor/ohtu-Alarmclock

https://github.com/isaacloud/android-alarm-clock

http://javatechig.com/android/repeat-alarm-example-in-android