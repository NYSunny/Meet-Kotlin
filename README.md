# Meet-Kotlin

## 自定义gradle配置文件
为了项目依赖方便管理和版本统一性，我们可以自定义gradle配置文件，把一些配置放入该文件中，然后全局引用，当我们需要修改依赖版本时，我们只需要修改我们
自定义的gradle配置文件，这样全局的依赖版本都会被统一修改。

### 如何自定义gradle配置文件：
在项目根目录下新建config.gradle(也可以是其他名字，但后缀必须是.gradle)，文件内容如下：
```
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
```
// 引入自定义gradle配置
apply from: "config.gradle"

buildscript {
    // ...
}
```
这样，项目中所有的module都可以引用这个自定义gradle文件中的配置。

我们在主module下的build.gradle文件中就可以这样使用自定义gradle文件中的配置：
```
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
```
// 引入自定义gradle配置
apply from: "../config.gradle"
```
那么在主module中就可以这样使用：
```
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
```
ext {

       channel = 'official'

}

ext {

       config = [
              channel : “official”
        ]

}
```
