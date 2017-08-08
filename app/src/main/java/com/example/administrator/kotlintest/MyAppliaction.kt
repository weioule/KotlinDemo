package com.example.administrator.kotlintest

import android.app.Application

/**
 * Created by wol on 2017/8/8.
 */

class MyAppliaction : Application() {

    companion object {
        public var phone: String? = null
        public var password: String? = null
        public var isRememberPassword: Boolean = false
    }

}
