package com.bezzo.callrecorder

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val CALL = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_call.setOnClickListener {
            if (PermissionUtil.requestCallPhonePermission(this)){
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:${et_number.text.toString()}")
                startActivityForResult(intent, CALL)
            }
        }

        btn_history_call.setOnClickListener {
            if (PermissionUtil.requestCallLogPermission(this)){
                var stringBuffer = StringBuffer()
                val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null,
                    null, android.provider.CallLog.Calls.DATE + " DESC") // add limit 1 jika ingin last
                val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
                val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)
                val type = cursor.getColumnIndex(CallLog.Calls.TYPE)
                stringBuffer.append("Call Details : \n")
                while (cursor.moveToNext()){
                    val phoneNumber = cursor.getString(number)
                    val callDuration = cursor.getString(duration)
                    val callType  = cursor.getString(type)
                    var typeCall = ""
                    typeCall = if (callType.toInt() == CallLog.Calls.OUTGOING_TYPE){
                        "Telepon Keluar"
                    } else if (callType.toInt() == CallLog.Calls.INCOMING_TYPE){
                        "Telepon Masuk"
                    } else {
                        callType.toString()
                    }

                    stringBuffer.append("Phone Number : $phoneNumber\n")
                    stringBuffer.append("Duration : $callDuration detik\n")
                    stringBuffer.append("Type : $typeCall\n")
                    stringBuffer.append("-------------------------------\n")
                }
                cursor.close()
                tv_history_call.text = stringBuffer
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                CALL -> {
                    Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
