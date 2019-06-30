Thumb
=====


[English-doc](https://github.com/Aeiric/thumb/blob/master/README-cn.md)

Thumb 是一个开源安卓端使用的高效的媒体图片加载库，包含视频解码，内存以及文件以及任务缓存，是一个轻量级库。

![](http://ww1.sinaimg.cn/large/a2e0153egy1g4lz7u5qctg20a00hsb2b.gif)

Thumb 支持从本地视频中高效获取桢图片，同样也是一个很适合练习多图片滑动展示的库，内存占用较少并且流畅高。它的兼容性也不错。

下载
--------
可以直接从该仓库clone源码使用，这是个轻量级库，核心代码都在lib包目录下


混淆
--------
不需要混淆

怎么使用?
-------------------
可以直接使用，UI相关核心逻辑在ThumbFragment类中。如果有样式调整可以根据需求自由定制修改。


状态
------
目前master分支是保护分支，欢迎提issure以及发合并申请

兼容性
-------------

API 10 以上都可以使用，不存在兼容性问题

构建demo APK
-----
使用gradle构建demo apk的式例代码:

```shell
git clone git@github.com:Aeiric/thumb.git # 
cd glide
git submodule init && git submodule update
./gradlew assembleDebug
```
**注意**: 确保你*Android SDK*和*Android Support Repository* 已安装，并确保本地Android环境正常可用

开发
-----------

暂无

遇到问题
------------
提交issure或者直接发merge request申请

贡献代码
------------
在提交代码之前, 贡献者必须遵循谷歌的[个人贡献者许可协议](https://developers.google.com/open-source/cla/individual).

感谢
------
暂无

作者
------
Aeiric 邮箱:xujian2065@163.com

使用须知协议
-------
GNU, 详情请查看 [GUN LICENSE](https://www.gnu.org/licenses/translations.html).


