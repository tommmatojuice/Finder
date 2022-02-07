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
import com.example.projectfinder.ui.DBClasses.Match
import com.example.projectfinder.ui.DBClasses.User
import kotlinx.android.synthetic.main.notification_card.view.*

class NotificationsAdapter(
    context: Context,
    private var list: List<Match>?,
    private val user: User,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<NotificationsAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.notification_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getMatch(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = list?.size?:0

    fun getMatch(position: Int): Match? = list?.get(position)

    fun setFiles(list: List<Match>){
        this.list = list
    }

    inner class ViewHolder(matchView: View) : RecyclerView.ViewHolder(matchView), View.OnClickListener
    {
        private val title: TextView = matchView.title_view
        private val info: TextView = matchView.info_view
        private val card: CardView = matchView.card_view
        private val image: ImageView = matchView.image_icon

        fun bind(version: Match)
        {
            title.text = version.projectTitle
            if (version.username == user.username)
                info.text = "Вы подходите для проекта"
            else
                info.text = "Кандидату понравился ваш проект"
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