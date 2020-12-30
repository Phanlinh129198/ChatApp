package com.rikkei.tranning.chatapp.views.uis.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.rikkei.tranning.chatapp.services.models.LoginUserModel
import com.rikkei.tranning.chatapp.services.models.UserModel
import com.rikkei.tranning.chatapp.services.repositories.SignUpRepository

class SignUpViewModel : ViewModel() {
    var firebaseAuth = FirebaseAuth.getInstance()
    var userEmail = MutableLiveData<String>()
    var userPass = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var signUpSuccess = MutableLiveData<Boolean>()

    var signUpStatus = MutableLiveData<SignUpStatus>()

    fun signUpButtonOnClick() {
        val user = LoginUserModel(userEmail.value!!, userPass.value!!)

        if (!user.validateEmail()) {
            signUpStatus.value = SignUpStatus.ErrorEmail(true);
        } else if(!user.validatePass()){
            signUpStatus.value = SignUpStatus.ErrorPass(true);
        } else {
            createUserFireBase()
        }
    }


    fun resetStatus() {
        signUpStatus.value = SignUpStatus.Loading(false)
    }

    fun createUserFireBase() {

        signUpStatus.value = SignUpStatus.Loading(true)
        firebaseAuth.createUserWithEmailAndPassword(
            userEmail.value!!,
            userPass.value!!
        )
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val userId = firebaseUser!!.uid
                val user = UserModel(
                    userId,
                    userName.value,
                    userEmail.value,
                    "default",
                    "default",
                    "default",
                    "offline"
                )
                SignUpRepository()
                    .createUserFromFirebase(user, userId)
                signUpSuccess.value = true
                signUpStatus.value = SignUpStatus.IsOk(true)
                signUpStatus.value = SignUpStatus.Loading(false)
            }
            .addOnFailureListener {
                signUpStatus.value = SignUpStatus.Failure(it)
                signUpStatus.value = SignUpStatus.IsOk(false)
                signUpStatus.value = SignUpStatus.Loading(false)
            }
    }

    sealed class SignUpStatus {

        data class Loading(var loading: Boolean) : SignUpStatus()

        data class IsOk(var isLogin: Boolean) : SignUpStatus()

        data class Failure(var e: Throwable) : SignUpStatus()

        data class ErrorEmail(var isError: Boolean) : SignUpStatus()
        data class ErrorPass(var isError: Boolean) : SignUpStatus()
    }
}