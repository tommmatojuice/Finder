package com.example.projectfinder.ui.registration

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
import com.example.projectfinder.ui.DBClasses.Contact
import com.example.projectfinder.ui.DBClasses.Login
import com.example.projectfinder.ui.DBClasses.SkillTag
import com.example.projectfinder.ui.DBClasses.User
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences
import com.example.projectfinder.util.ToastMessages
import kotlinx.android.synthetic.main.reg_contacts_page.view.*
import kotlinx.android.synthetic.main.reg_contacts_page.view.main_layout
import kotlinx.android.synthetic.main.reg_contacts_page.view.progress_bar
import kotlinx.android.synthetic.main.reg_contacts_page.view.progress_layout

class RegContactsFragment : Fragment() {
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.reg_contacts_page, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        progressBar = view.progress_bar
        progressLayout = view.progress_layout
        progressBar.isVisible = false
        progressLayout.isVisible = false

        initButtons(view)

        return view
    }

    private fun initButtons(view: View) {
        view.reg_button?.setOnClickListener {
            val email = view.email_field?.editText?.text.toString()
            val telegram = view.telegram_field?.editText?.text.toString()
            val site = view.site_field?.editText?.text.toString()

            when {
                checkEmail(email) -> {
                    activity?.applicationContext?.let {
                        ToastMessages.showMessage(
                            it,
                            "Неверно задан email"
                        )
                    }
                }
                checkURL(site) -> {
                    activity?.applicationContext?.let {
                        ToastMessages.showMessage(
                            it,
                            "Неверно задан веб-сайт"
                        )
                    }
                }
                else -> {
                    val user = arguments?.getBoolean("sex")?.let { it1 ->
                        User(
                            arguments?.getString("password").toString(),
                            listOf(
                                SkillTag("FastAPI"),
                                SkillTag("Django"),
                                SkillTag("ReactJS"),
                                SkillTag("Express")
                            ),
                            arguments?.getString("login").toString(),
                            arguments?.getString("name").toString(),
                            arguments?.getString("lastname").toString(),
                            Contact(email, telegram, site),
                            it1,
                            arguments?.getString("birthday").toString(),
                            arguments?.getString("location").toString(),
                            "",
                            "",
                            ""
                        )
                    }
                    user?.let { it1 -> addUser(it1) }
                }
            }
        }
    }

    private fun addUser(user: User) {
        val apiService = RestApiService()
        apiService.addUser(user, progressBar, progressLayout) {
            val loginPair = user.password?.let { it1 -> Login(it1, user.username, "") }
            loginPair?.let { it1 -> enter(it1) }
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

    private fun checkEmail(email: String): Boolean {
        return !Regex("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}\$").matches(
            email
        )
    }

    private fun checkURL(url: String): Boolean {
        return !Regex("(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?://(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})").matches(
            url
        )
    }
}

