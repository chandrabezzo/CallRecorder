package com.bezzo.callrecorder

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/**
 * Created by Lafran on 10/30/17.
 */
object PermissionUtil {
    const val PERMISSION_CALL_PHONE = 122
    const val PERMISSION_CALL_LOG = 123

    fun requestCallPhonePermission(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        val permission = Manifest.permission.CALL_PHONE
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (isNotGranted(context, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                    showAlertDialog(context, context.getString(R.string.title_permission),
                        "Ijinkan aplikasi untuk mengakses call phone?",
                        arrayOf(permission), PERMISSION_CALL_PHONE)
                } else {
                    requestPermission(context, arrayOf(permission), PERMISSION_CALL_PHONE)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    fun requestCallLogPermission(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        val permission = Manifest.permission.READ_CALL_LOG
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (isNotGranted(context, permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                    showAlertDialog(context, context.getString(R.string.title_permission),
                        "Ijinkan aplikasi untuk mengakses call phone?",
                        arrayOf(permission), PERMISSION_CALL_LOG)
                } else {
                    requestPermission(context, arrayOf(permission), PERMISSION_CALL_LOG)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    fun showAlertDialog(context: Context, title: String, message: String, permissions: Array<String>, flag: Int) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle(title)
        alertBuilder.setMessage(message)
        alertBuilder.setPositiveButton(android.R.string.yes) { dialog, which ->
            ActivityCompat.requestPermissions(context as Activity, permissions, flag)
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun isNotGranted(context: Context, manifestPermission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, manifestPermission) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(context: Activity, permissions: Array<String>, flag: Int) {
        ActivityCompat.requestPermissions(context, permissions, flag)
    }
}