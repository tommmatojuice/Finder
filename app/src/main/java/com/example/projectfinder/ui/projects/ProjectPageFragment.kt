package com.example.projectfinder.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projectfinder.R
import com.example.projectfinder.ui.DBClasses.ProjectsItem
import kotlinx.android.synthetic.main.project_page.view.*

class ProjectPageFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.project_page, container, false)

        val project: ProjectsItem = arguments?.getSerializable("project") as ProjectsItem

        if (arguments?.getInt("page") == 1){
            view.liner_layout.visibility = View.GONE
        } else {
            view.email_field.editText?.setText(if(project.user.contact.email == "") "Не задано" else project.user.contact.email)
            view.email_field.editText?.isFocusable = false
            view.telegram_field.editText?.setText(if(project.user.contact.telegram == "") "Не задано" else project.user.contact.telegram)
            view.telegram_field.editText?.isFocusable = false
            view.site_field.editText?.setText(if(project.user.contact.website == "") "Не задано" else project.user.contact.website)
            view.site_field.editText?.isFocusable = false
        }

        view.title_view.text = project.title
        view.description_view.text = if(project.description == "") "Описание проекта отсутсвует" else project.description
        view.image_text.text = project.title.first().toString()
        if (project.location == "")
            view.location_field.text = "Локация не задана"
        else  view.location_field.text = project.location
        if (project.canRemote)
            view.remote_field.text = "Можно удаленно"
        else  view.remote_field.text = "В офисе"

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val bundle = Bundle()
            if (arguments?.getInt("page") == 1){
                bundle.putSerializable("position", arguments?.getInt("position"))
                view?.let { Navigation.findNavController(it).navigate(R.id.navigation_projects, bundle) }
            } else {
                view?.let { Navigation.findNavController(it).navigate(R.id.navigation_notifications, bundle) }
            }
        }
    }
}