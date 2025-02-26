package com.azamovme.callmanageservice.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

fun getAppIconByPackageName(context: Context, packageName: String): Drawable? {
    val packageManager: PackageManager = context.packageManager
    try {
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        return packageManager.getApplicationIcon(appInfo)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return null
}

fun hideApp(context: Context) {
    val packageName = context.packageName
    val pm = context.packageManager

    // Ilovani yashirish
    try {
//        pm.setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun showApp(context: Context) {
    val packageName = context.packageName
    val pm = context.packageManager

    // Ilovani ko'rsatish
    try {
        pm.setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
