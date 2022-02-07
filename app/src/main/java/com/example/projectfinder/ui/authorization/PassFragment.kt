package com.example.projectfinder.ui.authorization

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.projectfinder.AppActivity
import com.example.projectfinder.R
import com.example.projectfinder.ui.DBClasses.Login
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences
import com.example.projectfinder.util.ToastMessages
import kotlinx.android.synthetic.main.pass_page.view.*
import kotlinx.android.synthetic.main.pass_page.view.progress_bar
import kotlinx.android.synthetic.main.pass_page.view.progress_layout

class PassFragment : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pass_page, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        if (savedInstanceState != null) {
            view?.auth_pass_field?.editText?.setText(savedInstanceState.getString("pass"))
        }

        progressBar = view.progress_bar
        progressLayout = view.progress_layout
        progressBar.isVisible = false
        progressLayout.isVisible = false

        initButtons(view)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("pass", view?.auth_pass_field?.editText?.text.toString())
    }

    private fun initButtons(view: View) {
        view.enter_button?.setOnClickListener {
            val login = arguments?.get("login").toString()
            val password = view.auth_pass_field?.editText?.text.toString()

            if (password.isEmpty()) {
                activity?.applicationContext?.let {
                    ToastMessages.showMessage(
                        it,
                        "Введите пароль"
                    )
                }
            } else {
                val loginPair = Login(password, login, "")
                enter(loginPair)
            }
        }
    }

    private fun enter(login: Login) {
        view?.main_layout?.visibility = View.INVISIBLE
        progressBar.isVisible = true
        progressLayout.isVisible = true
        val apiService = RestApiService()
        apiService.authUser(login, progressBar, progressLayout) {
            if (it != null) {
                mySharePreferences.setAccessToken(it.accessToken)
                mySharePreferences.setRefreshToken(it.refreshToken)
                mySharePreferences.setLogin(login.username)
                mySharePreferences.setPassword(login.password)

                val intent = Intent(activity, AppActivity::class.java)
                startActivity(intent)
                (activity as Activity?)?.overridePendingTransition(0, 0)
            } else {
                view?.main_layout?.visibility = View.VISIBLE
                progressBar.isVisible = false
                progressLayout.isVisible = false
                activity?.applicationContext?.let {
                    ToastMessages.showMessage(
                        it,
                        "Неверный логин или пароль"
                    )
                }
            }
        }
    }
}
