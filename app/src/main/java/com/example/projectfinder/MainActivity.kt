package com.example.projectfinder

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projectfinder.ui.DBClasses.Login
import com.example.projectfinder.ui.registration.RegStartFragment
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences

class MainActivity : AppCompatActivity()
{
    private val TAG = "MainActivity"
    private val simpleFragmentTag= "myFragmentTag"
    private var myFragment: Fragment? = null
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        mySharePreferences = MySharePreferences(this)

        initFragments()
    }

    private fun initFragments()
    {
        when {
            mySharePreferences.getAccessToken() == "" -> {
                myFragment = RegStartFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_frag, myFragment as RegStartFragment, simpleFragmentTag)
                    .commit()
            }
            !hasConnection(this) -> {
                myFragment = NoConnectionFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_frag, myFragment as NoConnectionFragment, simpleFragmentTag)
                    .commit()
            }
            else -> {
                enter()
            }
        }
    }

    private fun enter()
    {
        val apiService = RestApiService()
        apiService.authUser(
            Login(
                mySharePreferences.getPassword().toString(),
                mySharePreferences.getLogin().toString(), ""
            ), null, null
        ){
            if (it != null) {
                mySharePreferences.setAccessToken(it.accessToken)
                mySharePreferences.setRefreshToken(it.refreshToken)
                val intent = Intent(this, AppActivity::class.java)
                startActivity(intent)
            } else enter()
        }
    }

    private fun hasConnection(context: Context): Boolean
    {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifiInfo != null && wifiInfo.isConnected) {
            return true
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (wifiInfo != null && wifiInfo.isConnected) {
            return true
        }
        wifiInfo = cm.activeNetworkInfo
        return wifiInfo != null && wifiInfo.isConnected
    }
}