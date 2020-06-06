# SmsFilter
Android Message Filter Application

## Featurers
1. Support edting of filterring rules;

2. Support filterring by sender number, regex and received time.

# IMPORTANT
经测试，没能在MX5（android 5.1）拦截短信。
从android4.4开始无法通过abortBroadcast()拦截短信广播。

# 可能的解决方法
[Make your app the default SMS app](https://www.cnblogs.com/wangyk517/p/5881160.html)
> only the app that receives the SMS_DELIVER_ACTION broadcast (the user-specified default SMS app) is able to write to the SMS Provider 


