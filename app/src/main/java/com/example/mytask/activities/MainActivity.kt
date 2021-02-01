package com.example.mytask.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytask.R
import com.example.mytask.adapters.TaskAdapter
import com.example.mytask.models.ListModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_popup.*

class MainActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private  var adapter: RecyclerView.Adapter<TaskAdapter.ViewHolder>? = null
    var data:MutableList<ListModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UIevents()
        laodData ()

    }

    private fun UIevents () {
        btnAdd.setOnClickListener {
            var myDialog = Dialog(this)
            myDialog.setContentView(R.layout.layout_popup)
            myDialog.tvPopup.text = "Hello World!"
            myDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
            myDialog.show()
        }
    }


    private  fun laodData (){
        var language = arrayOf("Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com","Ruby", "Kotlin","apple.com")

        for ((i, item) in language.withIndex()){
            println(i)
            data.add(ListModel(item, i))
        }
        layoutManager = LinearLayoutManager(this)
        rvTask.layoutManager = layoutManager
        adapter = TaskAdapter(data,this)
        rvTask.adapter = adapter
    }


}