package com.limitlesser.bestpractice.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.tbruyelle.rxpermissions2.RxPermissions
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat

private val permissionDescribe = mapOf(
        Manifest.permission.CAMERA to "使用相机",
        Manifest.permission.WRITE_EXTERNAL_STORAGE to "访问设备上的照片和文件",
        Manifest.permission.RECORD_AUDIO to "使用麦克风")

@JvmOverloads
fun Activity.checkPermissions(vararg permission: String,
                              granted: (() -> Unit)? = null,
                              showRationale: ((Activity, Array<out String>, () -> Unit) -> Unit)? = null,
                              denied: (Activity) -> Unit = { it.onPermissionDenied() }) {

    val rxPermissions = RxPermissions(this)
    val notGranted = permission.filter { !rxPermissions.isGranted(it) }.toTypedArray()
    if (notGranted.isEmpty()) {
        granted?.invoke()
        return
    }

    val request: () -> Unit = {
        rxPermissions
                .requestEach(*notGranted).toList()
                .subscribe { list ->
                    when {
                        list.all { it.granted } -> granted?.invoke()
                        list.none { it.shouldShowRequestPermissionRationale } -> denied(this)
                    }
                }
    }

    if (showRationale != null && notGranted.any { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }) {
        showRationale(this, notGranted, request)
    } else {
        request()
    }
}

fun Context.isPermissionGranted(vararg permission: String) = permission.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

fun Activity.onPermissionDenied() {
    alert(Appcompat) {
        message = "您拒绝了权限申请,请前往设置页面开启权限"
        negativeButton("取消") {}
        positiveButton("去设置") {
            val toSetting = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.fromParts("package", packageName, null))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (toSetting.resolveActivity(packageManager) != null)
                startActivity(toSetting)
        }
    }.show()
}
