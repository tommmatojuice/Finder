package com.example.projectfinder.ui.profile

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.DatePicker
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projectfinder.R
import com.example.projectfinder.ui.DBClasses.Contact
import com.example.projectfinder.ui.DBClasses.SkillTag
import com.example.projectfinder.ui.DBClasses.User
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences
import com.example.projectfinder.util.ToastMessages
import kotlinx.android.synthetic.main.profile_page.view.*
import java.util.*

class EditProfileFragment : Fragment(), DatePickerDialog.OnDateSetListener
{
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var myUser: User
    private lateinit var birthday: String
    private var sex: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val appBar = (activity as AppCompatActivity?)?.supportActionBar
        appBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        appBar?.setDisplayHomeAsUpEnabled(true)
        appBar?.setDisplayShowHomeEnabled(true)
        appBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFFFF")))
        appBar?.title = Html.fromHtml("<font color='#000000'>Профиль</font>")
        appBar?.show()
        val view = inflater.inflate(R.layout.profile_page, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!
        getMyInfo(view)

        initUI(view)

        return view
    }

    private fun getMyInfo(view: View) {
        val apiService = RestApiService()
        apiService.myInfo("Bearer ${mySharePreferences.getAccessToken().toString()}", null, null) {
            if (it != null) {
                myUser = it
            }
            view.name_field.editText?.setText(it?.name)
            view.lastname_field.editText?.setText(it?.lastname)
            view.email_field.editText?.setText(it?.contact?.email)
            view.telegram_field.editText?.setText(it?.contact?.telegram)
            view.site_field.editText?.setText(it?.contact?.website)
            view.info_field.editText?.setText(it?.information)
            view.location_field.editText?.setText(it?.location)
            view.birthday_field.editText?.setText(it?.birthDate)
            birthday = it?.birthDate.toString()
            if (it?.gender!!)
                view.femail_check.isChecked = true
            else view.mail_check.isChecked = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_info -> {
                saveInfo()
                true
            }
            android.R.id.home -> {
                (activity as AppCompatActivity?)?.supportActionBar?.hide()
                view?.let { Navigation.findNavController(it).navigate(R.id.navigation_profile) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initUI(view: View)
    {
        view.birthday_field.editText?.isFocusable = false

        val radioGroup = view.radio_group
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            view.findViewById<RadioButton>(checkedId)?.apply {
                sex = text == "Женский"
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

    private fun saveInfo()
    {
        val apiService = RestApiService()

        val user = User(
            null,
            listOf(
                SkillTag("FastAPI"),
                SkillTag("Django"),
                SkillTag("ReactJS"),
                SkillTag("Express")
            ),
            myUser.username,
            view?.name_field?.editText?.text.toString(),
            view?.lastname_field?.editText?.text.toString(),
            Contact(
                view?.email_field?.editText?.text.toString(),
                view?.telegram_field?.editText?.text.toString(),
                view?.site_field?.editText?.text.toString()
            ),
            sex,
            birthday,
            view?.location_field?.editText?.text.toString(),
            view?.info_field?.editText?.text.toString(),
            "",
            ""
        )
        apiService.editMyInfo("Bearer ${mySharePreferences.getAccessToken().toString()}", user) {
            if (it != null)
                activity?.applicationContext?.let {
                    ToastMessages.showMessage(
                        it,
                        "Изменения сохранены"
                    )
                }
            else
                activity?.applicationContext?.let {
                    ToastMessages.showMessage(
                        it,
                        "Что-то пошло не так"
                    )
                }
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        view?.birthday_field?.editText?.setText("$day.${month+1}.$year")
        birthday = "$day.${month+1}.$year"
    }
}