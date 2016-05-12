# 兼容小米MIUI系统的Toast


## Usage

1: 引入依赖库，目前项目已经上传到JCenter，因此只需要在你的项目Module中直接引入即可:

```gradle
    dependencies {
        compile 'io.github.zhitaocai:toastcompat:0.1.0'
    }
```

2: 调用方法

```java
    ToastCompat.make(Context context, String text, long duration).show();
```

ToastCompat实现了自定义的[IToast](https://github.com/zhitaocai/ToastCompat/blob/master/app%2Fsrc%2Fmain%2Fjava%2Fio%2Fgithub%2Fzhitaocai%2Ftoastcompat%2Ftoastcompat%2FIToast.java)接口，IToast的接口方法基本和原来的Toast相差无几


## Blog

* [解决小米MIUI系统上后台应用没法弹Toast的问题](http://caizhitao.com/2016/02/09/android-toast-compat/)
