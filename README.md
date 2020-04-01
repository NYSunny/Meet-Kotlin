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

### 异形屏适配

首先，我们先明确刘海屏(包括水滴屏，以下我们统称为刘海屏)适配的标准接口(Google官方接口)是在Android P(Android 9.0)上提供的，在Android P且有刘海屏的手机上，
我们只需要实现Google提供的标准接口就能实现对刘海屏的适配(国内各大手机厂商在Android P上也遵从了Google标准API)，但是在Android P以前且有刘海屏的手机上怎么办
呢？没办法，我们还得适配各家手机厂商。

#### Android P及以上系统异形屏适配

Android P 提供了 3 种显示模式供开发者选择，分别是：
- 默认模式（LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT）
- 刘海区绘制模式（ LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES）
- 刘海区不绘制模式（LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER）

**默认模式（LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT）**
为了在不影响操作的情况下，尽可能利用刘海屏的显示区域，有以下表现：
|     | 非全屏(normal mode) | 全屏(fullscreen mode) |
| --- |        ---         |         ---          |
| 竖屏(portrait mode) | 使用耳朵区 | 禁用耳朵区 |
| 横屏（landscape mode） | 禁用耳朵区 | 禁用耳朵区 |

注：所谓全屏（fullscreen mode），是指隐藏状态栏（status bar），即通过 SYSTEM_UI_FLAG_FULLSCREEN 实现的效果。

示例代码：
```kotlin
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val lp = window.attributes
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.attributes = lp
    }
```

Android P之后Android提供了标准的接口用于获取异形屏的安全区域：
```kotlin
    window.decorView.post {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val displayCutout = window.decorView.rootWindowInsets?.displayCutout
            i("安全区域距离屏幕左边的距离：${displayCutout?.safeInsetLeft}")
            i("安全区域距离屏幕右边的距离：${displayCutout?.safeInsetRight}")
            i("安全区域距离屏幕顶部的距离：${displayCutout?.safeInsetTop}")
            i("安全区域距离屏幕底部的距离：${displayCutout?.safeInsetBottom}")

            val rects = displayCutout?.boundingRects
            if (rects.isNullOrEmpty()) {
                i("不是刘海屏")
            } else {
                i("刘海屏数量：${rects.size}")
                for (rect in rects) {
                    i("刘海屏区域：$rect")
                }
            }
        }
    }
```
注意这里要post到主线程的handler中。

输出：
```
2020-03-30 15:23:09.613 17184-17184/com.johnny.meet_kotlin I/Meet-Kotlin: 安全区域距离屏幕左边的距离：0
2020-03-30 15:23:09.613 17184-17184/com.johnny.meet_kotlin I/Meet-Kotlin: 安全区域距离屏幕右边的距离：0
2020-03-30 15:23:09.613 17184-17184/com.johnny.meet_kotlin I/Meet-Kotlin: 安全区域距离屏幕顶部的距离：88
2020-03-30 15:23:09.613 17184-17184/com.johnny.meet_kotlin I/Meet-Kotlin: 安全区域距离屏幕底部的距离：88
2020-03-30 15:23:09.613 17184-17184/com.johnny.meet_kotlin I/Meet-Kotlin: 刘海屏数量：2
2020-03-30 15:23:09.613 17184-17184/com.johnny.meet_kotlin I/Meet-Kotlin: 刘海屏区域：Rect(342, 0 - 738, 88)
2020-03-30 15:23:09.613 17184-17184/com.johnny.meet_kotlin I/Meet-Kotlin: 刘海屏区域：Rect(342, 2072 - 738, 2160)
```

#### Android P以下系统的异形屏适配

需要查看各家手机厂商提供的文档。
不想翻看文档的可以移步这篇文章：[Android P 刘海屏适配全攻略](https://juejin.im/post/5b1930835188257d7541ba33)

## 动画

### View-Animation

View-Animation，视图动画，顾名思义，这种动画只能作用于View上，主要分为两类：补间动画(Tween-Animation)和逐帧动画(Frame-Animation)。

#### 补间动画(Tween-Animation)

根据不用的动画效果，分为4种动画：
| 名称 | 原理 | 对应Animation子类 |
| --- | --- | --- |
| 平移动画(translate) | 移动View的位置 | TranslateAnimation类 |
| 旋转动画(rotate) | 旋转View的角度 | RotateAnimation类 |
| 缩放动画(scale) | 放大/缩小View的大小 | ScaleAnimation类 |
| 透明动画(alpha) | 改变View的透明度 | AlphaAnimation类 |

##### 具体使用

补间动画的使用方式分为两种：在`XML`代码 / `Java`代码里设置。

**平移动画(TranslateAnimation)**

**设置方法1：在`XML`代码中设置**

- 步骤1：在`res/anim`的文件夹里创建动画效果`.xml`文件
```
此处路径为res/anim/translate_animation.xml
```

- 步骤2：根据不同动画效果的语法设置不同动画参数，从而实现动画效果。平移动画效果设置具体如下：
```
<?xml version="1.0" encoding="utf-8"?>
// 采用<translate /> 标签表示平移动画
<translate xmlns:android="http://schemas.android.com/apk/res/android"

    // 以下参数是4种动画效果的公共属性,即都有的属性
    android:duration="3000" // 动画持续时间（ms），必须设置，动画才有效果
    android:startOffset ="1000" // 动画延迟开始时间（ms）
    android:fillBefore = “true” // 动画播放完后，视图是否会停留在动画开始的状态，默认为true
    android:fillAfter = “false” // 动画播放完后，视图是否会停留在动画结束的状态，优先于fillBefore值，默认为false
    android:fillEnabled= “true” // 是否应用fillBefore值，对fillAfter值无影响，默认为true
    android:repeatMode= “restart” // 选择重复播放动画模式，restart代表正序重放，reverse代表倒序回放，默认为restart|
    android:repeatCount = “0” // 重放次数（所以动画的播放次数=重放次数+1），为infinite时无限重复
    android:interpolator = @[package:]anim/interpolator_resource // 插值器，即影响动画的播放速度,下面会详细讲
    
    // 以下参数是平移动画特有的属性
    // 下面值有三种表示方式：
    // 1.绝对值，即下面这种，表示多少像素 
    // 2.带%号的，表示相对于自己的宽高来计算移动的起始和结束位置
    // 3.带%p的，表示相对于父容器的宽高计算移动的起始和结束位置
    // 注意：坐标系以当前View的视图坐标系为准，即当前View左上角为原点，不管是带%号的，还是带%p的，都以当前View的左上角为参照点
    // 移动百分之多少的距离，具体是谁的百分之多少就要看带不带p了，不带p的表示自己，带p表示父容器
    android:fromXDelta="0" // 视图在水平方向x 移动的起始值
    android:toXDelta="500" // 视图在水平方向x 移动的结束值
    android:fromYDelta="0" // 视图在竖直方向y 移动的起始值
    android:toYDelta="500" // 视图在竖直方向y 移动的结束值
    />
```

- 步骤3：在Java代码中创建`Animation`对象并播放动画
```kotlin
val animation = AnimationUtils.loadAnimation(this, R.anim.translate_animation)
sub_view.startAnimation(animation)
```

**设置方法2：在`Java`代码中设置**
TranslateAnimation常用的两个构造方法：
```
public TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
public TranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue, int fromYType, float fromYValue, int toYType, float toYValue)
```
第二个构造方法中多出了四个参数，表示值类型，有三种：`Animation.ABSOLUTE`、`Animation.RELATIVE_TO_SELF`、`Animation.RELATIVE_TO_PARENT`。
`Animation.ABSOLUTE`表示绝对值，任何值都可以，`Animation.RELATIVE_TO_SELF`、`Animation.RELATIVE_TO_PARENT`这两种Type的值只能是`0f~1.0f`，
最终它们是需要乘以宽度和高度才是动画移动的起始和结束值。

示例：
```kotlin
val anim = TranslateAnimation(
            Animation.ABSOLUTE,
            0F,
            Animation.ABSOLUTE,
            500F,
            Animation.ABSOLUTE,
            0F,
            Animation.ABSOLUTE,
            500F
)
anim.duration = 3000
sub_view.startAnimation(anim)
```

其余动画细节看这篇文章：[Android 动画：手把手教你使用 补间动画 (视图动画)](https://blog.csdn.net/carson_ho/article/details/72827747)

理解补间动画可以通过以下几点：
- 坐标系以View左上角为原点，向右为x轴正方向，向下为y轴正方向
- 带%是以自己的宽高为参照物，带%p是以父容器的宽高为参照物，然后在上述坐标系中左右上下移动

#### 逐帧动画(Frame-Animation)

[Android 逐帧动画：关于 逐帧动画 的使用都在这里了！](https://blog.csdn.net/carson_ho/article/details/73087488)

### 属性动画

[Android 属性动画：这是一篇很详细的 属性动画 总结&攻略](https://blog.csdn.net/carson_ho/article/details/72909894)

### 插值器和估值器

[Android 动画：你真的会使用插值器与估值器吗？（含详细实例教学）](https://blog.csdn.net/carson_ho/article/details/72863901)

### Android布局动画和转场动画

[Android动画总结——布局动画、转场动画](https://www.jianshu.com/p/4e7bbe57ac8d)
[Android 转场动画](https://juejin.im/post/5b0e8554f265da08ed7a15d1)









