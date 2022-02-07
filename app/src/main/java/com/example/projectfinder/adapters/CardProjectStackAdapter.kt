package com.example.projectfinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinder.R
import com.example.projectfinder.ui.DBClasses.ProjectsItem
import com.example.projectfinder.ui.DBClasses.ProjectsList
import kotlinx.android.synthetic.main.project_card.view.*

class CardProjectStackAdapter(
    private val list: ProjectsList,
    private val onItemClicked: (position: Int) -> Unit
) :
    RecyclerView.Adapter<CardProjectStackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.project_card, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun setData(projectItem: ProjectsItem) {
            val imageText = itemView.image_text
            val nameView = itemView.title_view
            val infoView = itemView.description_view
            val locationView = itemView.location_field
            val remoteView = itemView.remote_field

            imageText.text = projectItem.title.first().toString()
            nameView.text = projectItem.title
            val description = if(projectItem.description == "") "Описание проекта отсутсвует" else projectItem.description
            if (description.length > 500) {
                infoView.text = "${description.substring(0, 200)}..."
            } else infoView.text = description
            if (projectItem.location == "")
                locationView.text = "Локация не задана"
            else  locationView.text = projectItem.location
            if (projectItem.canRemote)
                remoteView.text = "Можно удаленно"
            else  remoteView.text = "В офисе"
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    fun getList(): ProjectsList {
        return list
    }
}
