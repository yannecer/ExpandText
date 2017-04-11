# ExpandText
可展开的TextView,文字展开之后，如果最后一行能容纳箭头，则箭头放在最后一行，如果文本刚好充满最后一行，箭头放在下一行


### 截图
![](https://github.com/yannecer/ExpandText/blob/master/app/ezgif-3-b1b39ede01.gif)


### 使用
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:necer="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    >
    <necer.expandtextview.ExpandTextView
        android:id="@+id/expand_text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#ffffff"
        necer:duration="200"
        necer:showLine="4"
        necer:textSize="20sp"
        necer:bitmapRightOffet="10dp"
        />
</LinearLayout>  

需要添加背景android:background=""，不然ViewGroup的onDraw()不会执行，就不会绘制箭头

```
### 自定义属性
   
duration：折叠动画时间  
showLine：折叠显示的行数  
textSize：字体大小  
textColor:字体颜色  
bitmapRightOffet：箭头距view右侧的距离  

