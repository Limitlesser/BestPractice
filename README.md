# BestPractice
android tools collection

* spannable

```
span {
        +"text" + color(Color.RED) { bold { "red" } } +
                italic { "italic" } +
                url("https://www.baidu.com") { "百度一下" } +
                click({ print("clicked") }, 0, 2) { "点击这里" }
    }
```

* recyclerView
```
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
                onItemClick = { data, _ -> Toast.makeText(this@MainActivity, data, Toast.LENGTH_SHORT).show() })
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