package com.example.projectfinder.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.projectfinder.MainActivity
import com.example.projectfinder.R
import com.example.projectfinder.adapters.ProjectsListAdapter
import com.example.projectfinder.ui.DBClasses.RefreshPair
import com.example.projectfinder.ui.DBClasses.User
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment(), ProjectsListAdapter.OnItemClickListener
{
    private lateinit var mySharePreferences: MySharePreferences
    private var adapter: ProjectsListAdapter? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: LinearLayout
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        progressBar = view.progress_bar
        progressLayout = view.progress_layout
        progressBar.isVisible = true
        progressLayout.isVisible = true

        refreshToken()
        getMyInfo(view)
        initButtons(view)

        return view
    }

    private fun initButtons(view: View)
    {
        view.edit_button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigation_edit_profile)
        }

        view.add_button.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("user", this.user)
            bundle.putSerializable("previous", 2)
            Navigation.findNavController(view).navigate(R.id.navigation_edit_project, bundle)
        }

        view.exit_text.setOnClickListener {
            mySharePreferences.setPassword("")
            mySharePreferences.setLogin("")
            mySharePreferences.setRefreshToken("")
            mySharePreferences.setAccessToken("")
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            (activity as Activity?)?.overridePendingTransition(0, 0)
        }
    }

    private fun getMyInfo(view: View)
    {
        val apiService = RestApiService()
        apiService.myInfo("Bearer ${mySharePreferences.getAccessToken().toString()}", progressBar, progressLayout) {
            if (it != null){
                this.user = it
                view.name_view.text = "${it?.name} ${it?.lastname}"
                view.image_text.text = it.name.first().toString()
                if (it.information.isEmpty())
                    view.description_view.text = "Вы еще ничего не написали о себе. Самое время сделать это!"
                else view.description_view.text = it.information
            }
        }

        apiService.myProjects("Bearer ${mySharePreferences.getAccessToken().toString()}", null, null) { projectsList ->
            adapter = this.context?.let { ProjectsListAdapter(it, projectsList, this) }
            val list = view.projects_recycle_view
            list.adapter = adapter

            val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
            decoration.setDrawable(activity?.applicationContext?.let {
                ContextCompat.getDrawable(
                    it,
                    R.color.white
                )
            }!!)
            list.addItemDecoration(decoration)
        }
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("project", adapter?.getProject(position))
        bundle.putSerializable("user", this.user)
        bundle.putInt("previous", 2)
        view?.let { Navigation.findNavController(it).navigate(R.id.navigation_edit_project, bundle) }
    }

    private fun refreshToken() {
        val apiService = RestApiService()
        val pair = mySharePreferences.getRefreshToken()?.let { RefreshPair("", it) }
        pair?.let {
            apiService.refreshToken(it) {
                if (it != null) {
                    mySharePreferences.setAccessToken(it.accessToken)
                    mySharePreferences.setRefreshToken(it.refreshToken)
                }
            }
        }
    }
}