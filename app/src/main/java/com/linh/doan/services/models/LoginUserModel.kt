package com.linh.doan.services.models

import android.util.Patterns
import java.util.regex.Pattern

class LoginUserModel(var email: String, var password: String) {

    fun validateEmail(): Boolean {
        //return true;
        val a = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return  Pattern.compile(a).matcher(email).matches();
    }
    fun validatePass(): Boolean {
        //số,chữ,kí tự đặc biệt >6 kí tự
        val a = "^(?=.*[0-9])(?=.*[a-z])(?=.*[\\\\\\/%§\"&“|`´}{°><:.;#')(@_\$\"!?*=^-]).{6,}\$";
        return  Pattern.compile(a).matcher(password).matches();

    }

    val isEmailValid: Boolean
        get() = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val isPasswordLengthGreaterThan5: Boolean
        get() = password.length > 5

}