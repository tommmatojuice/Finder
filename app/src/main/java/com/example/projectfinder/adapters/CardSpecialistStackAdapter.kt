package com.example.projectfinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinder.R
import com.example.projectfinder.ui.DBClasses.User
import kotlinx.android.synthetic.main.specialist_card.view.*

class CardSpecialistStackAdapter(
    private val list: List<User>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<CardSpecialistStackAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.specialist_card, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        init {
            itemView.setOnClickListener(this)
        }

        fun setData(user: User) {
            val imageText = itemView.image_text
            val nameView = itemView.name_view
            val infoView = itemView.info_view

            imageText.text = user.name.first().toString()
            nameView.text = "${user.name} ${user.lastname}"
            val info = if(user.information == "") "Информация о себе отсутсвует" else user.information
            if (info.length > 500) {
                infoView.text = "${info.substring(0, 200)}..."
            } else infoView.text = info
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    fun getList(): List<User> {
        return list
    }
}

