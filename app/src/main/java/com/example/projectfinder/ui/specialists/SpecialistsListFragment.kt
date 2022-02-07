package com.example.projectfinder.ui.specialists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.projectfinder.R
import com.example.projectfinder.adapters.CardSpecialistStackAdapter
import com.example.projectfinder.ui.DBClasses.ProjectsItem
import com.example.projectfinder.ui.DBClasses.RefreshPair
import com.example.projectfinder.ui.DBClasses.SlugUser
import com.example.projectfinder.ui.retrofit.RestApiService
import com.example.projectfinder.util.MySharePreferences
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.login_page.view.*
import kotlinx.android.synthetic.main.specialists_list.view.*
import kotlinx.android.synthetic.main.specialists_list.view.progress_bar
import kotlinx.android.synthetic.main.specialists_list.view.progress_layout

class SpecialistsListFragment : Fragment()
{
    private val TAG = "SpecialistsListFragment"
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardSpecialistStackAdapter
    private lateinit var cardStackView: CardStackView
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var project: ProjectsItem
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.specialists_list, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        progressBar = view.progress_bar
        progressLayout = view.progress_layout
        progressBar.isVisible = true
        progressLayout.isVisible = true

        project = arguments?.getSerializable("project") as ProjectsItem
        view?.title_text?.text = "Выбранный проект — ${project.title}"

        refreshToken()

        cardStackView = view.card_stack_view
        manager = CardStackLayoutManager(this.context, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=" + direction?.name + " ratio=" + ratio)
            }

            override fun onCardSwiped(direction: Direction?) {
                Log.d(TAG, "onCardSwiped: p=" + manager.topPosition + " d=" + direction)
                manager.topPosition

                val apiService = RestApiService()
                if (direction?.name == "Right"){
                    apiService.likeUser(
                        "Bearer ${mySharePreferences.getAccessToken().toString()}",
                        SlugUser(adapter.getList()[manager.topPosition - 1].username, project.slug)
                    ){
                        Log.d("likeUser", it.toString())
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
        getUsersList()
        cardStackView.layoutManager = manager
        cardStackView.itemAnimator = DefaultItemAnimator()

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("login", view?.auth_login_field?.editText?.text.toString())
    }

    private fun getUsersList() {
        val apiService = RestApiService()
        apiService.usersList(
            "Bearer ${mySharePreferences.getAccessToken().toString()}",
            project,
            progressBar,
            progressLayout
        ) {
            if (it != null) {
                adapter = CardSpecialistStackAdapter(it) { position ->
                    view?.let { it1 ->
                        onListItemClick(
                            position,
                            it1
                        )
                    }
                }
            }
            cardStackView.adapter = adapter
        }
    }

    private fun onListItemClick(position: Int, view: View) {
        val bundle = Bundle()
        bundle.putSerializable("specialist", adapter.getList()[position])
        bundle.putSerializable("position", manager.topPosition)
        bundle.putSerializable("project", this.project)
        bundle.putSerializable("page", 1)
        Navigation.findNavController(view).navigate(R.id.navigation_specialist_page, bundle)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.let { Navigation.findNavController(it).navigate(R.id.navigation_specialists) }
        }
    }
}