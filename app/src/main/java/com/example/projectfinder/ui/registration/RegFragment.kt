package com.example.projectfinder.ui.registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.example.projectfinder.R
import com.example.projectfinder.util.MySharePreferences
import com.example.projectfinder.util.ToastMessages
import kotlinx.android.synthetic.main.reg_page.view.*
import java.util.*

class RegFragment : Fragment(), DatePickerDialog.OnDateSetListener
{
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var birthday: String
    private var sex: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.reg_page, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        initButtons(view)
        initUI(view)

        return view
    }

    private fun initUI(view: View)
    {
        view.birthday_field.editText?.isFocusable = false

        val radioGroup = view.radio_group
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            view.findViewById<RadioButton>(checkedId)?.apply {
                if (text == "Женский")
                    sex = true
            }
        }

        view.birthday_button.setOnClickListener {
            val datePicker = this.context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
            }
            datePicker?.show()
        }
    }

    private fun initButtons(view: View) {
        view.continue_button?.setOnClickListener {
            val name = view.name_field?.editText?.text.toString()
            val lastName = view.lastname_field?.editText?.text.toString()
            val location = view.location_field?.editText?.text.toString()

            if (name.isEmpty() || lastName.isEmpty() || birthday.isEmpty()) {
                activity?.applicationContext?.let {
                    ToastMessages.showMessage(
                        it,
                        "Все поля должны быть заполнены!"
                    )
                }
            } else {
                val bundle = Bundle()
                val regContactsFragment = RegContactsFragment()
                bundle.putString("password", arguments?.getString("password").toString())
                    .apply { bundle.putString("login", arguments?.getString("login").toString()) }
                    .apply { bundle.putString("name", name)}
                    .apply { bundle.putString("lastname", lastName)}
                    .apply { bundle.putBoolean("sex", sex)}
                    .apply { bundle.putString("birthday", birthday)}
                    .apply { bundle.putString("location", location)}
                regContactsFragment.arguments = bundle
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.main_frag, regContactsFragment)
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        view?.birthday_field?.editText?.setText("$day.${month+1}.$year")
        birthday = "$day.${month+1}.$year"
    }
}