package com.linh.doan.views.uis

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.linh.doan.R
import com.linh.doan.helper.LocaleHelper
import com.linh.doan.views.uis.friend.SharedFriendViewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    var chatViewModel: SharedFriendViewModel? = null

    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatViewModel =
            ViewModelProviders.of(this).get(SharedFriendViewModel::class.java)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayoutChat, MainFragment(), null)
        fragmentTransaction.commit()
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, 10)
        }
        val languageToLoad = LocaleHelper.getLanguage(this)
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
        LocaleHelper.setBaseContex(baseContext)
    }

    override fun onRestart() {
        super.onRestart()
        chatViewModel?.updateStatus("status", "online")

    }

    override fun onPause() {
        super.onPause()
        chatViewModel?.updateStatus("status", "offline")

    }


    fun hideKeyBoardMain(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}