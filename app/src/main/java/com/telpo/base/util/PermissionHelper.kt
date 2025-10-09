package com.telpo.base.util

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val activity: AppCompatActivity) {

    interface PermissionCallback {
        fun onAllPermissionsGranted()
        fun onPermissionDenied(deniedPermissions: List<String>)
    }

    // 需要动态申请的常规权限
    private val normalPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )

    // 需要特殊处理的权限（跳转系统设置）
    private val specialPermissions = mapOf(
        // 文件管理权限 (Android 11+)
        "MANAGE_EXTERNAL_STORAGE" to Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION),
        // 悬浮窗权限
        "SYSTEM_ALERT_WINDOW" to Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${activity.packageName}")
        )
    )

    fun checkAllPermissions(callback: PermissionCallback) {
        if (checkSpecialPermissions()) {
            requestNormalPermissions(callback)
        }
    }

    private fun checkSpecialPermissions(): Boolean {
        var allGranted = true

        // 检查文件管理权限（Android 11+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
            !Environment.isExternalStorageManager()) {
            showSpecialPermissionDialog(
                title = "文件访问权限",
                message = "请开启文件访问权限，否则无法正常使用本应用！",
                intent = specialPermissions["MANAGE_EXTERNAL_STORAGE"]!!,
                onCancel = { allGranted = false }
            )
        }

//        // 检查悬浮窗权限
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//            !Settings.canDrawOverlays(activity)) {
//            showSpecialPermissionDialog(
//                title = "悬浮窗权限",
//                message = "请开启悬浮窗权限，否则无法正常使用本应用！",
//                intent = specialPermissions["SYSTEM_ALERT_WINDOW"]!!,
//                onCancel = { allGranted = false }
//            )
//        }

        return allGranted
    }

    private fun requestNormalPermissions(callback: PermissionCallback) {
        val ungrantedPermissions = normalPermissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (ungrantedPermissions.isEmpty()) {
            callback.onAllPermissionsGranted()
            return
        }

        val shouldShowRationale = ungrantedPermissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

        if (shouldShowRationale) {
            showRationaleDialog(ungrantedPermissions, callback)
        } else {
            ActivityCompat.requestPermissions(
                activity,
                ungrantedPermissions.toTypedArray(),
                NORMAL_PERMISSION_REQUEST_CODE
            )
        }
    }

    // 处理权限请求结果
    fun handlePermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        callback: PermissionCallback
    ) {
        if (requestCode != NORMAL_PERMISSION_REQUEST_CODE) return

        val deniedPermissions = normalPermissions.filterIndexed { index, _ ->
            grantResults.getOrNull(index) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            callback.onAllPermissionsGranted()
        } else {
            callback.onPermissionDenied(deniedPermissions)
        }
    }

    // 统一对话框管理
    private fun showSpecialPermissionDialog(
        title: String,
        message: String,
        intent: Intent,
        onCancel: () -> Unit
    ) {
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("确定") { _, _ -> activity.startActivity(intent) }
            .setNegativeButton("取消") { _, _ -> onCancel() }
            .setCancelable(false)
            .show()
    }

    private fun showRationaleDialog(
        permissions: List<String>,
        callback: PermissionCallback
    ) {
        AlertDialog.Builder(activity)
            .setTitle("需要权限")
            .setMessage("请授予以下权限以正常使用功能：\n${getPermissionNames(permissions)}")
            .setPositiveButton("确定") { _, _ ->
                ActivityCompat.requestPermissions(
                    activity,
                    permissions.toTypedArray(),
                    NORMAL_PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton("取消") { _, _ -> callback.onPermissionDenied(permissions) }
            .show()
    }

    private fun getPermissionNames(permissions: List<String>): String {
        return permissions.joinToString("\n") {
            when (it) {
                Manifest.permission.CAMERA -> "相机"
                Manifest.permission.READ_EXTERNAL_STORAGE -> "读取存储"
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> "写入存储"
                else -> ""
            }
        }
    }

    companion object {
        const val NORMAL_PERMISSION_REQUEST_CODE = 1001
    }
}