package com.example.projectfinder.ui.projects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.projectfinder.R
import com.example.projectfinder.adapters.CardProjectStackAdapter
import com.example.projectfinder.ui.DBClasses.*
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_project.view.*

class ProjectsFragment : Fragment() {
    private val TAG = "ProjectsFragment"
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardProjectStackAdapter
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var cardStackView: CardStackView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_project, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        progressBar = view.progress_bar
        progressLayout = view.progress_layout
        progressBar.isVisible = true
        progressLayout.isVisible = true

        refreshToken()

        cardStackView = view.card_stack_view

        manager = CardStackLayoutManager(this.context, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=" + direction?.name + " ratio=" + ratio)
            }

            override fun onCardSwiped(direction: Direction?) {
                adapter.getList()[manager.topPosition - 1].slug
                Log.d(
                    TAG,
                    "onCardSwiped: p=" + adapter.getList()[manager.topPosition - 1].slug + " d=" + direction
                )
                val apiService = RestApiService()
                if (direction?.name == "Right"){
                    apiService.likeProject(
                        "Bearer ${mySharePreferences.getAccessToken().toString()}",
                        Slug(adapter.getList()[manager.topPosition - 1].slug)
                    ){
                        Log.d("likeProject", it.toString())
                    }
                }
            }

            override fun onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.topPosition)
            }

            override fun onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.topPosition)
            }

            override fun onCardAppeared(view: View?, position: Int) {
                Log.d(TAG, "onCardAppeared: $position")
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                Log.d(TAG, "onCardAppeared: $position")
            }
        })
        if (arguments?.getInt("position") != null)
            manager.topPosition = arguments?.getInt("position")!!
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator())
        if (savedInstanceState != null) {
            manager.topPosition = savedInstanceState.getInt("topPosition")
        }
        getProjectsList()
        cardStackView.layoutManager = manager
        cardStackView.itemAnimator = DefaultItemAnimator()

        return view
    }

    private fun onListItemClick(position: Int, view: View) {
        val bundle = Bundle()
        bundle.putSerializable("project", adapter.getList()[position])
        bundle.putSerializable("position", manager.topPosition)
        bundle.putSerializable("page", 1)
        Navigation.findNavController(view).navigate(R.id.navigation_project_page, bundle)
    }

    private fun getProjectsList() {
        val apiService = RestApiService()
        apiService.projectsList(
            "Bearer ${mySharePreferences.getAccessToken().toString()}",
            Shit(0, 20),
            progressBar,
            progressLayout
        ) {
            if (it != null) {
                adapter = CardProjectStackAdapter(it) { position ->
                    view?.let { it1 ->
                        onListItemClick(
                            position,
                            it1
                        )
                    }
                }
                cardStackView.adapter = adapter
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