package com.example.projectfinder.ui.specialists

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
import com.example.projectfinder.R
import com.example.projectfinder.adapters.ProjectsListAdapter
import com.example.projectfinder.ui.DBClasses.RefreshPair
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences
import kotlinx.android.synthetic.main.fragment_specialists.view.add_button
import kotlinx.android.synthetic.main.fragment_specialists.view.progress_bar
import kotlinx.android.synthetic.main.fragment_specialists.view.progress_layout
import kotlinx.android.synthetic.main.fragment_specialists.view.projects_recycle_view

class SpecialistsFragment : Fragment(), ProjectsListAdapter.OnItemClickListener
{
    private var adapter: ProjectsListAdapter? = null
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_specialists, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        progressBar = view.progress_bar
        progressLayout = view.progress_layout
        progressBar.isVisible = true
        progressLayout.isVisible = true

        refreshToken()
        getMyProjects(view)
        initButtons(view)

        return view
    }

    private fun initButtons(view: View)
    {
        view.add_button.setOnClickListener {
            val apiService = RestApiService()
            apiService.myInfo("Bearer ${mySharePreferences.getAccessToken().toString()}", progressBar, progressLayout) {
                if (it != null){
                    val bundle = Bundle()
                    bundle.putSerializable("user", it)
                    bundle.putSerializable("previous", 1)
                    Navigation.findNavController(view).navigate(R.id.navigation_edit_project, bundle)
                }
            }
        }
    }

    private fun getMyProjects(view: View)
    {
        val apiService = RestApiService()
        apiService.myProjects("Bearer ${mySharePreferences.getAccessToken().toString()}", progressBar, progressLayout) { projectsList ->
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

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("project", adapter?.getProject(position))
        view?.let { Navigation.findNavController(it).navigate(R.id.navigation_specialists_list, bundle) }
    }
}