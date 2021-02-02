package com.example.mytask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytask.R
import com.example.mytask.activities.MainActivity
import com.example.mytask.database.User

class TaskAdapter(
    private val data:  List<User>,
    var taskInterface: MainActivity
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(parent.context ).inflate(R.layout.task_layout,parent,false)
        return  ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = data[position].firstName
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitle : TextView = itemView.findViewById(R.id.tvTask)

        init {
            itemView.setOnClickListener {
                var position = adapterPosition
                var task = data[position].firstName
                var uid = data[position].uid

                if (task != null) {
                    taskInterface?.btnClicked(position,task,uid)
                }
            }
        }
    }

    fun updateData(taskList: ArrayList<User>, user: User) {
        taskList.add(user)
    }
}