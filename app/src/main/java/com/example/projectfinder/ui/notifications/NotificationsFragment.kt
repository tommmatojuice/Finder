package com.example.projectfinder.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.projectfinder.R
import com.example.projectfinder.adapters.NotificationsAdapter
import com.example.projectfinder.ui.DBClasses.RefreshPair
import com.example.projectfinder.ui.DBClasses.User
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class NotificationsFragment : Fragment(), NotificationsAdapter.OnItemClickListener
{
    private lateinit var mySharePreferences: MySharePreferences
    private var adapter: NotificationsAdapter? = null
    private lateinit var user: User
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: LinearLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        mySharePreferences = context?.let { MySharePreferences(it) }!!
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        activity?.findViewById<FrameLayout>(R.id.main_frag)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.nav_host_fragment)?.visibility = View.VISIBLE

        progressBar = view.progress_bar
        progressLayout = view.progress_layout
        progressBar.isVisible = true
        progressLayout.isVisible = true

        refreshToken()
        getMyMatches(view)

        return view
    }

    private fun getMyMatches(view: View)
    {
        val apiService = RestApiService()
        apiService.myInfo("Bearer ${mySharePreferences.getAccessToken().toString()}", null, null) {
            if (it != null){
                this.user = it
                apiService.myMatches("Bearer ${mySharePreferences.getAccessToken().toString()}", progressBar, progressLayout) { matches ->
                    if (matches != null){
                        view.notifications_count.text = matches.size.toString()
                        adapter = this.context?.let { NotificationsAdapter(it, matches, user, this) }
                        val list = view.notifications_recycler_view
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
            }
        }
    }

    override fun onItemClick(position: Int) {
        val apiService = RestApiService()
        val match= adapter?.getMatch(position)
        if (user.username == match?.username){
            apiService.getProject("Bearer ${mySharePreferences.getAccessToken().toString()}", match.slug, null, null){
                val bundle = Bundle()
                bundle.putSerializable("project", it)
                bundle.putSerializable("page", 2)
                view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.navigation_project_page, bundle) }
            }
        } else {
            match?.username?.let {
                apiService.getUser("Bearer ${mySharePreferences.getAccessToken().toString()}",
                    it, null, null){user ->
                    val bundle = Bundle()
                    bundle.putSerializable("specialist", user)
                    bundle.putSerializable("page", 2)
                    view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.navigation_specialist_page, bundle) }
                }
            }
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
}