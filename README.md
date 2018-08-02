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

//
```