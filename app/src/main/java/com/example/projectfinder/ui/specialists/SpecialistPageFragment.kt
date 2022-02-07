package com.example.projectfinder.ui.specialists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projectfinder.R
import com.example.projectfinder.ui.DBClasses.ProjectsItem
import com.example.projectfinder.ui.DBClasses.User
import kotlinx.android.synthetic.main.specialist_page.view.*

class SpecialistPageFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.specialist_page, container, false)

        val specialist: User = arguments?.getSerializable("specialist") as User

        if (arguments?.getInt("page") == 1){
            view.liner_layout.visibility = View.GONE
        } else {
            view.email_field.editText?.setText(if(specialist.contact.email == "") "Не задано" else specialist.contact.email)
            view.email_field.editText?.isFocusable = false
            view.telegram_field.editText?.setText(if(specialist.contact.telegram == "") "Не задано" else specialist.contact.telegram)
            view.telegram_field.editText?.isFocusable = false
            view.site_field.editText?.setText(if(specialist.contact.website == "") "Не задано" else specialist.contact.website)
            view.site_field.editText?.isFocusable = false
        }

        view.name_view.text = "${specialist.name} ${specialist.lastname}"
        view.info_view.text = if(specialist.information == "") "Информация о себе отсутсвует" else specialist.information
        view.image_text.text = specialist.name.first().toString()

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val bundle = Bundle()
            if (arguments?.getInt("page") == 1){
                bundle.putSerializable("position", arguments?.getInt("position"))
                bundle.putSerializable("project", arguments?.getSerializable("project") as ProjectsItem)
                view?.let { Navigation.findNavController(it).navigate(R.id.navigation_specialists_list, bundle) }
            } else {
                view?.let { Navigation.findNavController(it).navigate(R.id.navigation_notifications, bundle) }
            }
        }
    }
}