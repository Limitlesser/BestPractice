package com.limitlesser.bestpractice.context

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import java.io.File


abstract class ScreenShotContentObserver(private val context: Context,
                                         handler: Handler = Handler(Looper.getMainLooper())) : ContentObserver(handler) {

    private var isFromEdit = false
    private var previousPath: String? = null

    //有些手机会收到注册监听之前的事件，加个注册时间过滤掉之前的消息
    var registerTime = System.currentTimeMillis()


    override fun onChange(selfChange: Boolean, uri: Uri) {
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA), null, null, null)
            if (cursor?.moveToLast() == true) {
                val displayNameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val fileName = cursor.getString(displayNameColumnIndex)
                val path = cursor.getString(dataColumnIndex)
                val lastModified = File(path).lastModified()
                if (lastModified > registerTime && lastModified >= System.currentTimeMillis() - 10000) {
                    if (isScreenshot(path) && !isFromEdit && !(previousPath != null && previousPath == path)) {
                        onScreenShot(path, fileName)
                    }
                    previousPath = path
                    isFromEdit = false
                }
            }
        } catch (t: Throwable) {
            Log.e("ScreenShotObserver", "error", t)
            isFromEdit = true
        } finally {
            cursor?.close()
        }
        super.onChange(selfChange, uri)
    }

    private fun isScreenshot(path: String?): Boolean {
        return path != null && path.toLowerCase().contains("screenshot")
    }

    protected abstract fun onScreenShot(path: String, fileName: String)
}