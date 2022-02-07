package com.example.projectfinder.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.projectfinder.R
import com.example.projectfinder.ui.DBClasses.*
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences
import com.example.projectfinder.util.ToastMessages
import kotlinx.android.synthetic.main.project_info_page.view.*
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern

class EditProjectPage : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences
    private var project: ProjectsItem? = null
    private lateinit var user: User
    private val NONLATIN: Pattern = Pattern.compile("[^\\w-]")
    private val WHITESPACE: Pattern = Pattern.compile("[\\s]")

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
        appBar?.title = Html.fromHtml("<font color='#000000'>Проект</font>")
        appBar?.show()
        val view = inflater.inflate(R.layout.project_info_page, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        user = arguments?.getSerializable("user") as User
        if (arguments?.getSerializable("project") != null) {
            project = arguments?.getSerializable("project") as ProjectsItem
            view.title_field.editText?.setText(project?.title)
            view.info_field.editText?.setText(project?.description)
            view.location_field.editText?.setText(project?.location)
            if (project?.canRemote!!)
                view.remote_check.isChecked = true
        }

        return view
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
                if (arguments?.getInt("previous") == 2)
                    view?.let { Navigation.findNavController(it).navigate(R.id.navigation_profile) }
                else view?.let { Navigation.findNavController(it).navigate(R.id.navigation_specialists) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveInfo() {
        if (this.project != null) {
            editProject()
        } else {
            addProject()
        }
        if (arguments?.getInt("previous") == 2)
            view?.let { Navigation.findNavController(it).navigate(R.id.navigation_profile) }
        else view?.let { Navigation.findNavController(it).navigate(R.id.navigation_specialists) }
    }

    private fun editProject() {
        val apiService = RestApiService()
        val changeProject = this.project?.slug?.let {
            ProjectsItem(
                listOf(
                    SkillTag("FastAPI"),
                    SkillTag("Django"),
                    SkillTag("ReactJS"),
                    SkillTag("Express")
                ),
                view?.title_field?.editText?.text.toString(),
                view?.info_field?.editText?.text.toString(),
                view?.location_field?.editText?.text.toString(),
                view?.remote_check?.isChecked ?: false,
                it,
                this.user,
                null,
                null
            )
        }
        project?.slug?.let {
            changeProject?.let { it1 ->
                apiService.changeProject(
                    "Bearer ${mySharePreferences.getAccessToken().toString()}",
                    it, it1
                ) {
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
        }
    }

    private fun addProject() {
        val apiService = RestApiService()
        val newProject = ProjectsItem(
            listOf(
                SkillTag("FastAPI"),
                SkillTag("Django"),
                SkillTag("ReactJS"),
                SkillTag("Express")
            ),
            view?.title_field?.editText?.text.toString(),
            view?.info_field?.editText?.text.toString(),
            view?.location_field?.editText?.text.toString(),
            view?.remote_check?.isChecked ?: false,
            toSlug(view?.title_field?.editText?.text.toString()),
            this.user,
            null,
            null
        )
        apiService.createProject(
            "Bearer ${mySharePreferences.getAccessToken().toString()}",
            newProject
        ) {
            Log.d("newProject", it.toString())
        }
    }

    private fun toSlug(input: String?): String {
        val noSpace: String = WHITESPACE.matcher(input).replaceAll("-")
        val normalized: String = Normalizer.normalize(noSpace, Normalizer.Form.NFD)
        val slug: String = NONLATIN.matcher(normalized).replaceAll("")
        return slug.toLowerCase(Locale.ENGLISH)
    }
}