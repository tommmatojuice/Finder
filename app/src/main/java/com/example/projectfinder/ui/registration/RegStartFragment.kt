package com.example.projectfinder.ui.registration

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.projectfinder.R
import com.example.projectfinder.ui.authorization.LoginFragment
import com.example.projectfinder.util.ToastMessages
import kotlinx.android.synthetic.main.reg_start_page.view.*


class RegStartFragment : Fragment()
{
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.reg_start_page, container, false)

        if (savedInstanceState != null) {
            view?.login_field?.editText?.setText(savedInstanceState.getString("login"))
            view?.pass_field?.editText?.setText(savedInstanceState.getString("pass"))
        }

        view.continue_button.typeface = Typeface.createFromAsset(context?.assets, "sans_regular.ttf")

        initButtons(view)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("login", view?.login_field?.editText?.text.toString())
        outState.putString("pass", view?.pass_field?.editText?.text.toString())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initButtons(view: View) {
        val bundle = Bundle()
        view.continue_button?.setOnClickListener {
            val password = view.pass_field?.editText?.text.toString()
            val login = view.login_field?.editText?.text.toString()

            if (password.isEmpty() || login.isEmpty()) {
                activity?.applicationContext?.let { ToastMessages.showMessage(
                    it,
                    "Все поля должны быть заполнены"
                ) }
            } else {
                if (checkPass(password) != null){
                    activity?.applicationContext?.let { ToastMessages.showMessage(
                        it, checkPass(
                            password
                        ).toString()
                    ) }
                } else {
                    if (checkLogin(login) != null){
                        activity?.applicationContext?.let { ToastMessages.showMessage(
                            it, checkLogin(
                                login
                            ).toString()
                        ) }
                    } else {
                        val regFragment = RegFragment()
                        bundle.putString("password", password).apply { bundle.putString(
                            "login",
                            login
                        ) }
                        regFragment.arguments = bundle
                        activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.main_frag, regFragment)
                            ?.addToBackStack(null)
                            ?.commit()
                    }
                }
            }
        }

        view.auth_button?.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.main_frag, LoginFragment())
                    ?.addToBackStack(null)
                    ?.commit()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkPass(password: String): String? {
        if (!password.chars().anyMatch(Character::isUpperCase))
            return "Пароль должен содержать символы верхнего регистра"
        if (password.length < 8)
            return "Минимальная длина пароля 8 символов"
        if (password.length > 50)
            return "Максимальная длина пароля 50 символов"
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkLogin(login: String): String? {
        if (login.length < 8)
            return "Минимальная длина логина 8 символов"
        if (login.length > 50)
            return "Максимальная длина логина 50 символов"
        return null
    }
}