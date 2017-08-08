package com.example.administrator.kotlintest

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.ac_fragment_login.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    /**
     * 正则表达式:验证手机号
     */
    private val REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$"

    private var phone: String? = null
    private var password: String? = null
    private var etTilPhone: TextInputLayout? = null
    private var etTilPassword: TextInputLayout? = null
    private var rememberPassword: TextView? = null
    private var drawable: Drawable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_fragment_login)

        loginBtn.setOnClickListener(this)
        forget_password.setOnClickListener(this)
        remember_password.setOnClickListener(this)
        new_registered.setOnClickListener(this)

        rememberPassword = findViewById(R.id.remember_password) as TextView
        updateRememberIcon(MyAppliaction.isRememberPassword)

        etShow()

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.loginBtn -> {
                checkedUserInfo()
            }

            R.id.forget_password ->
                ToastUtils.showToast(this, getString(R.string.forgetPassword))

            R.id.new_registered ->
                ToastUtils.showToast(this, getString(R.string.new_registered))

            R.id.remember_password -> {
                MyAppliaction.isRememberPassword = !MyAppliaction.isRememberPassword
                updateRememberIcon(MyAppliaction.isRememberPassword)
            }
        }

    }

    //数据记录
    private fun etShow() {
        etTilPhone = findViewById(R.id.et_til_phone) as TextInputLayout
        if (MyAppliaction.phone != null) {
            etTilPhone!!.editText!!.setText(MyAppliaction.phone)
        } else {
            etTilPhone!!.hint = "手机号"
        }
        etTilPhone!!.isErrorEnabled = true

        etTilPassword = findViewById(R.id.et_til_password) as TextInputLayout
        if (MyAppliaction.password != null) {
            etTilPassword!!.editText!!.setText(MyAppliaction.password)
        } else {
            etTilPassword!!.hint = "密码"
        }
        etTilPassword!!.isErrorEnabled = true
    }

    //更新记住密码状态
    private fun updateRememberIcon(isRemember: Boolean) {
        if (isRemember) {
            //在左侧添加图片
            drawable = resources.getDrawable(R.drawable.icon_selected)
        } else {
            drawable = resources.getDrawable(R.drawable.icon_unselected)
        }
        drawable!!.setBounds(0, 0, drawable!!.minimumWidth, drawable!!.minimumHeight)
        rememberPassword!!.setCompoundDrawables(drawable, null, null, null)
    }

    //过滤用户信息
    private fun checkedUserInfo() {
        phone = etTilPhone!!.editText!!.getText().toString().trim()

        if (isMobile(phone!!)) {
            etTilPhone!!.isErrorEnabled = false
        } else {
            etTilPhone!!.error = "请输入正确的手机号"
            return
        }

        password = etTilPassword!!.editText!!.text.toString().trim()

        if (password!!.length < 6) {
            etTilPassword!!.error = "密码不能小于6位"
            return
        } else if (password!!.length > 16) {
            etTilPassword!!.error = "密码不能大于16位"
            return
        } else {
            etTilPassword!!.isErrorEnabled = false
        }

        login()
    }

    //模拟登录并记录数据
    private fun login() {
        Toast.makeText(this, phone + "  " + password, Toast.LENGTH_SHORT).show()
        MyAppliaction.phone = etTilPhone!!.editText!!.text.toString().trim()
        if (MyAppliaction.isRememberPassword) {
            MyAppliaction.password = etTilPassword!!.editText!!.text.toString().trim()
        } else {
            MyAppliaction.password = null
        }
    }

    //校验手机号
    fun isMobile(mobile: String): Boolean {
        return Pattern.matches(REGEX_MOBILE, mobile)
    }
}


