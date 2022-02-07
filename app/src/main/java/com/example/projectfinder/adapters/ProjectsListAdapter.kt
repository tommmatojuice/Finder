package com.example.projectfinder.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinder.R
import com.example.projectfinder.ui.DBClasses.ProjectsItem
import kotlinx.android.synthetic.main.notification_card.view.*

class ProjectsListAdapter(context: Context,
                          private var list: List<ProjectsItem>?,
                          private val listener: OnItemClickListener
): RecyclerView.Adapter<ProjectsListAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.notification_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getProject(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = list?.size?:0

    fun getProject(position: Int): ProjectsItem? = list?.get(position)

    inner class ViewHolder(projectView: View) : RecyclerView.ViewHolder(projectView), View.OnClickListener
    {
        private val title: TextView = projectView.title_view
        private val info: TextView = projectView.info_view
        private val card: CardView = projectView.card_view
        private val image: ImageView = projectView.image_icon

        fun bind(version: ProjectsItem)
        {
            title.text = version.title
            info.visibility = View.GONE
            image.setImageResource(R.drawable.ellipse)
        }

        init {
            card.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position:Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}