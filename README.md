# BestPractice
android tools collection

* spannable

```kotlin
span {
    +"text" + color(Color.RED) { bold { "red" } } +
            italic { "italic" } +
            url("https://www.baidu.com") { "百度一下" } +
            click({ print("clicked") }, 0, 2) { "点击这里" }
}
```

* recyclerView
```kotlin
//divider
addItemDecoration(dividerItemDecoration(context, inset = dip(51)))

//space
 addItemDecoration(SpaceItemDecoration(dip(12), true, true))
 
//adapter
baseAdapter<String> {
    item(R.layout.list_item,
            holder = ::ViewHolder,
            onBind = { data, _ ->
                itemView.text.text = data
            },
            onItemClick = { data, _ -> toast(data) })
}
adapter.submitList(listOf("haushdfa", "asdfsa", "sdfsfd", "sdfs", "sdfsf"))

//diff util
dispatchChanges(DiffCallback(old, files, UploadFile::id))

//endless scroll
endlessScroll = EndlessRecyclerViewScrollListener(layoutManager, { _, _, _ ->
    viewModel.loadFiles(true)
})
addOnScrollListener(endlessScroll)
    
```

* views
```kotlin
//imageView bitmap
val bitmap = imageView.bitmap

//visibility
view.isVisible = true

//children
val children = viewgroup.children

//menu items
val items = menu.items

//clipArc
progressBar.progressDrawable = ClipArcDrawable(cornerRadius = dip(2))

//bubble
msgContentView.background = bubbleStateListDrawable(mContext, bubbleColor,
                bubbleStrokeColor, bubbleStrokeWidth, isSender, showPressEffect)

val bubble = BubbleImageView(context)
bubble.arrowPosition = Arrow.Left


```

* RxBus
```kotlin
//post
RxBus.post(SubmitEvent())

//observe
RxBus.observe<SubmitEvent>()
         .bindToLifecycle(this)
         .subscribe { submit() }

//named event
RxBus.post(PushOrder.EVENT_TYPE_PUSH_STAFF, mapOf("count" to it.total))

RxBus.observe<Map<String, Any>>(PushOrder.EVENT_TYPE_PUSH_STAFF)
        .bindToLifecycle(this)
        .subscribe { badge.setNumber(it["count"] as Int) }
```

* RxJava
```kotlin
//async
orderApi.fetchGroup()
     .asyncIO()
     .subscribe({ groups = it }) { Log.e(TAG, it.localizedMessage) }

//loading
orderApi.orders().asyncIO()
    .onLoading { progressBar.isVisible = it }

```

* preferences
```kotlin
/**是否显示通知开关*/
var isShowNotification: Boolean by PrefDelegate(context, "isShowNotification", true)

/** 最近使用的emoji */
var recentEmotions: List<String> by PrefDelegate(context, "recentEmotions", emptyList())
```
* permissions
```kotlin
checkPermissions(Manifest.permission.CAMERA, granted = {
    qrCodeView.startCamera()
    qrCodeView.startSpot()
})
```
* progress result
```kotlin
mediasCache.downloadMediaWithProgress(data.url)
                .bindToLifecycle(this)
                .subscribe({
                    it.showProgress { isLoading, _ -> progress.isVisible = isLoading }
                    it.handleResult {
                        ...
                    }
                }
```

* Activity Result
```kotlin
val intent = Intent(this, Main2Activity::class.java)
startActivityForResult(intent)
           .subscribe {
               if (it.isOk)
                   Toast.makeText(this, "收到结果:${it.data?.getStringExtra("data")}", Toast.LENGTH_SHORT)
                           .show()
           }
```