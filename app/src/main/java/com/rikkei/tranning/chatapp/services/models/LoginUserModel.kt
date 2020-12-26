package com.rikkei.tranning.chatapp.services.models

import android.util.Patterns
import java.util.regex.Pattern

class LoginUserModel(var email: String, var password: String) {

    fun validateEmailPassword(): Boolean {

//        val a = Pattern.compile(
//                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
//                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
//                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
//                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
//                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
//            ).matcher(email).matches() && Pattern.compile(
//                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$"
//            ).matcher(password).matches()
//        return a
        return true;
    }

    val isEmailValid: Boolean
        get() = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val isPasswordLengthGreaterThan5: Boolean
        get() = password.length > 5

}