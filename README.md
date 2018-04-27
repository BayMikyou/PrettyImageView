# PrettyImageView
### 一、简述
这是一个Kotlin实现的简单小巧支持圆形和圆角定制化的ImageView。

### 二、实现功能所用到的知识

* 1、Kotlin中基本语法知识
* 2、Kotlin中自定义属性访问器
* 3、Kotlin中默认值参数实现构造器函数重载以及@JvmOverloads注解的使用
* 4、Kotlin标准库中常见的apply,run,with函数的使用
* 5、Kotlin中默认值参数函数的使用
* 6、自定义View的基本知识
* 7、Path的使用
* 8、Matrix的使用
* 9、BitmapShader的使用

### 三、PrettyImageView支持的功能

* 1、支持图片圆形的定制化
* 2、支持图片圆角以及每个角的X,Y方向值的定制化
* 3、支持形状边框宽度颜色的定制化
* 4、支持图片圆角或者圆形右上角消息圆点定制化(一般用于圆形或者圆角头像)

### 四、PrettyImageView自定义属性的含义

|开放属性name|开放属性含义|
|:---|:---|
|shape_type|形状类型,目前只有圆角和圆形两种类型|
|left_top_radiusX|左上角X轴方向半径|
|left_top_radiusY|左上角Y轴方向半径|
|right_top_radiusX|右上角X轴方向半径|
|right_top_radiusY|右上角Y轴方向半径|
|right_bottom_radiusX|右下角X轴方向半径|
|right_bottom_radiusY|右下角Y轴方向半径|
|left_bottom_radiusX|左下角X轴方向半径|
|left_bottom_radiusY|左下角Y轴方向半径|
|show_border|是否显示边框|
|border_width|边框宽度|
|border_color|边框颜色|
|show_circle_dot|是否显示右上角圆点|
|circle_dot_color|右上角圆点颜色|
|circle_dot_radius|右上角圆点半径|

### 五、运行效果

![](https://user-gold-cdn.xitu.io/2018/4/27/16302b71ae463e20?w=2000&h=2000&f=png&s=1366029)





