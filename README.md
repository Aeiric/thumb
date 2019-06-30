Thumb
=====


[中文文档](https://github.com/Aeiric/thumb/blob/master/README.md)

Thumb is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, it is lightweight.


![](http://ww1.sinaimg.cn/large/a2e0153egy1g4lz7u5qctg20a00hsb2b.gif)

Thumb supports get frame image from a local video,effective and also it is a good practice project for scrolling many kinds of a list of images as smooth and fast as possible.Use low memory and it's compatibility is good as well


Download
--------
You can just clone source code just from this pro,it is lightweight,core code is in package named lib



ProGuard
--------
no need to config ProGuard

How do I use Thumb?
-------------------
You can use it directly , core show code is in ThumbFragment,if you have some diffent show of UI ,just ignore ThumbFragment and create whatever ajust yourself.


Status
------
now is a is currently under development on the `master` branch.

Comments/bugs/questions/pull requests are always welcome! 

Compatibility
-------------
no compatibility problem,from API 10

Build Demo APK
-----
Building thumb demo apk with gradle is fairly straight forward:

```shell
git clone git@github.com:Aeiric/thumb.git # 
cd glide
git submodule init && git submodule update
./gradlew assembleDebug
```

**Note**: Make sure your *Android SDK* has the *Android Support Repository* installed, and that your `$ANDROID_HOME` environment
variable is pointing at the SDK or add a `local.properties` file in the root project with a `sdk.dir=...` line.


Development
-----------

no Development temporarily

Getting Help
------------
To report a specific problem or feature request, just use issure or make a merge request

Contributing
------------
Before submitting pull requests, contributors must sign Google's [individual contributor license agreement][7].


Thanks
------
no thanks temporarily

Author
------
Aeiric email:xujian2065@163.com

License
-------
GNU, See the [GUN LICENSE](https://www.gnu.org/licenses/translations.html)file for details.

