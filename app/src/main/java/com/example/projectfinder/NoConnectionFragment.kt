package com.example.projectfinder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.no_connection_page.view.*
import androidx.fragment.app.Fragment

class NoConnectionFragment: Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.no_connection_page, container, false)

        initButtons(view)

        return view
    }

    private fun initButtons(view: View){
        view.try_button.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            (activity as Activity?)?.overridePendingTransition(0, 0)
        }
    }
}