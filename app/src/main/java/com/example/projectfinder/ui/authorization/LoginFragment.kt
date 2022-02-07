package com.example.projectfinder.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectfinder.R
import com.example.projectfinder.ui.registration.RegStartFragment
import com.example.projectfinder.util.ToastMessages
import kotlinx.android.synthetic.main.login_page.view.*

class LoginFragment : Fragment()
{
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.login_page, container, false)

        if (savedInstanceState != null) {
            view?.auth_login_field?.editText?.setText(savedInstanceState.getString("login"))
        }

        initButtons(view)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("login", view?.auth_login_field?.editText?.text.toString())
    }

    private fun initButtons(view: View)
    {
        val bundle = Bundle()
        view.continue_auth_button?.setOnClickListener {
            val login = view.auth_login_field?.editText?.text.toString()

            if (login.isEmpty()){
                activity?.applicationContext?.let { ToastMessages.showMessage(it, "Введите логин") }
            } else {
                val passFragment = PassFragment()
                bundle.putString("login", login)
                passFragment.arguments = bundle
                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.main_frag, passFragment)
                        ?.addToBackStack(null)
                        ?.commit()
            }
        }

        view.reg_auth_button?.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.main_frag, RegStartFragment())
                    ?.addToBackStack(null)
                    ?.commit()
        }
    }
}

