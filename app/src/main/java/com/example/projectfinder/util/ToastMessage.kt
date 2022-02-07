package com.example.projectfinder.util

import android.content.Context
import android.widget.Toast

class ToastMessages
{
    companion object
    {
        fun showMessage(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}