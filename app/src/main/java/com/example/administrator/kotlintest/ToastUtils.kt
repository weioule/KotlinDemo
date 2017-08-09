package com.example.administrator.kotlintest

import android.content.Context
import android.widget.Toast


/**
 * 作者：wol
 * 时间：2016-10-15 16:23
 */

object ToastUtils {

    private var sToast: Toast? = null

    fun showToast(context: Context?, msg: String) {
        if (context == null) return
        if (sToast == null) {
            sToast = Toast.makeText(context.applicationContext, msg, Toast.LENGTH_SHORT)
        }
        //如果这个Toast已经在显示了，那么这里会立即修改文本
        sToast!!.setText(msg)
        sToast!!.show()
    }


}
