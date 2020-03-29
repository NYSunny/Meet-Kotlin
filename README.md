# Meet-Kotlin

## 自定义gradle配置文件
为了项目依赖方便管理和版本统一性，我们可以自定义gradle配置文件，把一些配置放入该文件中，然后全局引用，当我们需要修改依赖版本时，我们只需要修改我们
自定义的gradle配置文件，这样全局的依赖版本都会被统一修改。

### 如何自定义gradle配置文件：
在项目根目录下新建config.gradle(也可以是其他名字，但后缀必须是.gradle)，文件内容如下：
```groovy
ext {
    android = [
            compileSdkVersion: 29,
            minSdkVersion    : 19,
            targetSdkVersion : 29,
            versionCode      : 1,
            versionName      : "1.0"
            // ...
    ]
    deps = [
            // 单元测试依赖
            "junit"                             : "junit:junit:4.12",
            "androidx.test.ext"                 : "androidx.test.ext:junit:1.1.1",
            "androidx.test.espresso"            : "androidx.test.espresso:espresso-core:3.2.0",

            "androidx.appcompat"                : "androidx.appcompat:appcompat:1.1.0",
            "androidx.core-ktx"                 : "androidx.core:core-ktx:1.2.0",
            "androidx.androidx.constraintlayout": "androidx.constraintlayout:constraintlayout:1.1.3"
            // ...
    ]
}
```
当然这个自定义gradle文件的位置不是固定的，我们可以在根目录下新建一个`buildConfig`的文件夹，然后把这个文件放入其中，引用的时候
记得带上对应的文件夹名称即可。

### 如何引用自定义gradle配置文件
我们可以在项目根目录下的build.gradle文件中这样引用：
```groovy
// 引入自定义gradle配置
apply from: "config.gradle"

buildscript {
    // ...
}
```
这样，项目中所有的module都可以引用这个自定义gradle文件中的配置。

我们在主module下的build.gradle文件中就可以这样使用自定义gradle文件中的配置：
```groovy
defaultConfig {
        applicationId "com.johnny.meet_kotlin"
        minSdkVersion rootProject.ext.android["minSdkVersion"]
        // ...
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation rootProject.ext.deps["androidx.appcompat"]
    implementation rootProject.ext.deps["androidx.core-ktx"]
    // ...
}
```

当然，我们也可以把这个自定义gradle配置引入某个module，比如主module中，那么我们就需要这样引用：
```groovy
// 引入自定义gradle配置
apply from: "../config.gradle"
```
那么在主module中就可以这样使用：
```groovy
defaultConfig {
        applicationId "com.johnny.meet_kotlin"
        minSdkVersion android["minSdkVersion"]
        // ...
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation deps["androidx.appcompat"]
    implementation deps["androidx.core-ktx"]
    // ...
}
```

自定义gradle属性要注意以下两种区别：
```groovy
ext {

       channel = 'official'

}

ext {

       config = [
              channel : "official"
        ]

}
```

## 提升gradle构建速度的技巧
gradle构建的三个性能指标：
1.全量编译：所有代码和资源文件都会编译一遍
2.代码增量编译：修改了java/kotlin代码后的编译
3.资源增量编译：修改了res下的资源文件后的编译

- 使用最新版的gradle插件
- 避免激活旧的Multidex
64k问题，新版本的AS已经自带分包机制
- 调试时禁止多渠道apk构建
多渠道打包的时候我们可以一次性编译打包出所有渠道的apk包，但是我们调试的时候不需要这些，我们应该在调试阶段禁止这些多渠道apk的构建。

Preferences | Build, Execution, Deployment | Compiler 下的Command-line Options中输入`-PdevBuild`，然后在app module下的
build.gradle文件中像下面这样使用：
```groovy
android {

    // 调试
    if (rootProject.hasProperty('devBuild')) {
        splits.abi.enable = false
        splits.density.enable = false
    }
    // ...
}
```

- 最小化打包资源文件
我们的apk一般都会做许多适配工作，比如多语言，像素密度的适配，但是我们开发调试的时候可以有选择的省略一些资源的编译打包，从而提高编译速度。
具体使用如下：
```groovy
android {

    // 调试
    if (rootProject.hasProperty('devBuild')) {
        splits.abi.enable = false
        splits.density.enable = false
    }

    // ...

    defaultConfig {
        // ...

        // 调试，指定编译打包的资源文件，以下资源文件会被编译
        resConfigs("zh", "xxxhdpi")
    }
}
```

- 禁用PNG压缩
```groovy
android {

    // 调试
    if (rootProject.hasProperty('devBuild')) {
        splits.abi.enable = false
        splits.density.enable = false
        // 禁用PNG压缩
        aaptOptions.cruncherEnabled = false
    }
    // ...
}
```

- PNG转换WebP

- 推荐使用Instant run

- 不使用动态版本标识
以前我们在使用依赖库时，有时会看到依赖库后面的版本号后面有个`+`号，这就是动态版本的标识，标识使用依赖库的最新版本，这会降低
编译速度。

- gradle内存分配调优
在项目根目录下的gradle.properties中可以使用：
```
org.gradle.jvmargs=-Xmx1536m
```
属性来调整jvm的内存大小，通常大的内存会提高编译速度

- 开启gradle构建缓存
默认是不开启的，也是在gradle.properties中配置相关属性：
```
# 开启gradle构建缓存
org.gradle.caching=true
```

## 全面屏、刘海屏、水滴屏及沉浸式状态栏适配

### 全面屏适配

市面上大多数全面屏手机都是18:9的高宽比，有的可能会更高，比标准的16:9的高宽比在高度上要更高，Android提供了标准接口来声明应用支持的最大高宽比：
```xml
<application>
    <meta-data android:name="android.max_aspect" android:value="ratio_float" />
</application>
```

若开发者没有声明该属性，ratio_float的默认值是1.86，小于2.0，应用跑在高宽比大于这个值的全面屏手机上，默认不会全屏显示，底部会留黑，**考虑到越来越多的超长手机出现，
建议开发者将`Maximum Aspect Ratio ≥ 2.2`或更多，如果应用的`android:resizeableActivity`已经设置为`true`，就不必设置`Maximum Aspect Ratio`了**

### 刘海屏、水滴屏适配

首先，我们先明确刘海屏(包括水滴屏，以下我们统称为刘海屏)适配的标准接口(Google官方接口)是在Android P(Android 9.0)上提供的，在Android P且有刘海屏的手机上，
我们只需要实现Google提供的标准接口就能实现对刘海屏的适配(国内各大手机厂商在Android P上也遵从了Google标准API)，但是在Android P以前且有刘海屏的手机上怎么办
呢？没办法，我们还得适配各家手机厂商，例如：
小米有刘海屏且系统为Android O的手机对刘海屏的适配(https://dev.mi.com/console/doc/detail?pId=1293)

### 沉浸式状态栏适配

沉浸式状态栏一般是指透明状态栏，即页面内容或背景延伸到状态栏区域，有时候如果不是图片背景需要延伸到状态栏里面，能不使用沉浸式状态栏就不使用沉浸式状态栏，实现沉浸式
状态栏后可能后续会面临很多的适配工作，比如最基本的聊天会话页面，点击输入框会弹起输入法，这时需要聊天窗口整个上移到输入框上面，在沉浸式状态栏下，想要聊天窗口的ListView
上移需要手动测量窗口高度，而且在不同手机上的表现还不相同，相当麻烦。

透明状态栏只有在Android 4.4(API 19)之后才能设置，代码如下：
```kotlin
    val window = activity.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = Color.TRANSPARENT
        var systemUIFlags = activity.window.decorView.systemUiVisibility
        systemUIFlags =
            systemUIFlags or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        activity.window.decorView.systemUiVisibility = systemUIFlags
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
```

`window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)`设置的其实是半透明状态栏，并非完全透明，所以在Android 4.4的设备上你可能看到
的是半透明的状态栏，到了API 21之后，系统提供了标准的接口设置状态栏颜色，我们就可以通过上面的代码设置纯透明的状态栏了。注意：在API 21之后，如果仅设置
`window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)`同样显示的是半透明状态栏

说到上面的代码，有一点我们需要明确，只要实现了这几行代码：
```kotlin
var systemUIFlags = activity.window.decorView.systemUiVisibility
systemUIFlags =
     systemUIFlags or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
activity.window.decorView.systemUiVisibility = systemUIFlags
```
页面布局都会延伸到状态栏区域，不管状态栏设置的是什么颜色。所以上面这坨代码一般都会跟透明颜色一起设置。

如果我们只是单纯的想修改状态栏颜色，我们只需要像下面这样实现：
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    activity.window.statusBarColor = bg
}
```

其实这里最终显示的效果就不是沉浸式状态栏了，但是这里设置状态栏颜色仅支持API 21，如果应用适配到API 19，怎么办呢？这里提供一个思路，由于API 19没有提供设置状态栏颜色
的标准接口，但是API 19已经能够实现半透明状态栏了，我们可以在实现半透明状态栏的基础上在状态栏的位置添加占位View，然后给占位View设置背景颜色，但这里的颜色可能跟预想
的会不一样，毕竟占位View上面还有一层半透明状态栏在上面盖着。

### 状态栏黑色字符、白色字符的适配

在Android 6.0以前，android没有标准的api实现状态栏黑白字符的效果，因此，各厂商需要单独适配，但是在Android 6.0及以上版本，Android提供了标准的方法实现状态栏
黑白字符的效果：
```kotlin
/**
 * Android6.0及以上系统使用Android提供的标准api来修改状态栏黑白字符
 * Android6.0以下各家厂商需要单独适配
 *
 * @param lightMode true表示黑色字符，false表示白色字符
 */
fun setLightStatusBarIcon(activity: Activity, lightMode: Boolean) {
    val window = activity.window
    // Android 6.0以上
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        val vis = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = if (lightMode) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}
```
Android 6.0以下系统的黑白字符适配请移步各家厂商的开放平台：
[小米开放平台](https://dev.mi.com/console/doc/detail?pId=1293)
[vivo开放平台](https://dev.vivo.com.cn/documentCenter/doc/103)
[OPPO开放平台](https://open.oppomobile.com/wiki/doc#id=10161)
[魅族开放平台](http://open-wiki.flyme.cn/doc-wiki/index#id?79)

