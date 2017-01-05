# TestCustomTopBarShowHide

##Demo
滚动 WebView 时，在Activity中
上推慢慢透明(alpha)完全透明后隐藏(gone)其上的标题栏，
下拉则显示(visible)且慢慢不透明，
快速下拉或上推不会闪，
慢拉或慢推则会闪！
因为隐藏(gone)其上的标题栏会释放空间造成 webview 撑满 Activity 它会多次调整自身大小造成的

采用自定义视图（Scroller）实现，
在推拉webview时上下滑动自定义视图坐标系，
上推时将作为子视图的标题栏移出可见区域后隐藏(gone)，坐标系恢复屏幕坐标系原点，
下拉时先向上移动坐标系，显示子视图的标题栏并可见(visible)，然后向下滑动坐标系，
慢拉或慢推则会闪！
原因如上！

采用 FrameLayout + ObjectAnimator 实现，
标题栏浮于 listview 之上，listview 上用 TopPadding 来为标题栏留出空间，
用对象动画来实现标题栏的 Y 轴上的缩放，在动画结束时根据标题显隐来决定 listview 的 TopPadding 是为标题栏留出空间，
不会出现闪的情况！

![](https://github.com/wzhnsc/TestCustomTopBarShowHide/blob/master/gif/show.gif)
![](https://github.com/wzhnsc/TestCustomTopBarShowHide/blob/master/gif/show2.gif)
![](https://github.com/wzhnsc/TestCustomTopBarShowHide/blob/master/gif/show3.gif)
