package com.example.mytask.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.mytask.R
import com.example.mytask.adapters.TaskAdapter
import com.example.mytask.database.AppDatabase
import com.example.mytask.database.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_popup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity()  {

    var taskList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UIevents()
        laodData ()
    }

    private fun insertDatabase(msg:String, results: (User) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()
            val userDao = db.userDao()
            db.userDao().insertAll(User(null, msg, "Dhingra"))
            var last = (db.userDao().getLast()[0])
            results(last)
        }
    }

    private fun deleteDb(user:User, results: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()
            val userDao = db.userDao()
            db.userDao().delete(user)
            results(true)
        }
    }

    private fun updateDb(id:Int,text:String, results: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()
            val userDao = db.userDao()
            db.userDao().updateText(id,text)
            results(true)
        }
    }


    private fun UIevents () {
        btnAdd.setOnClickListener {
            var myDialog = Dialog(this)
            myDialog.setContentView(R.layout.layout_popup_add)
            myDialog.window?.setGravity(Gravity.TOP)
            myDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
            myDialog.show()

            var buttonAddTask = myDialog.findViewById(R.id.buttonAddTask) as Button
            var editTextAdd = myDialog.findViewById(R.id.editTextAdd) as EditText

            buttonAddTask.setOnClickListener {
                println(editTextAdd.text)
                insertDatabase("${editTextAdd.text}"){ res ->
                    runOnUiThread {
                        addToTask(res)
                    }
                }
                myDialog.dismiss()
            }
        }

    }

    fun addToTask(res:User){
        val taskView = findViewById<RecyclerView>(R.id.rvTask)
        taskList.add(res)
        (taskView.adapter as TaskAdapter).notifyDataSetChanged()
    }


    private  fun laodData (){
        loadTaskData()
    }

    @SuppressLint("WrongConstant")
    private fun displayTaskData(taskList: ArrayList<User>) {
        val taskView = findViewById<RecyclerView>(R.id.rvTask)
        taskView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        taskView.adapter = TaskAdapter(taskList,this)
    }

    private fun loadTaskData() {
        CoroutineScope(Dispatchers.IO).launch {
            taskList.clear()
            val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").build()
            for(element in database.userDao().getAll()){
                taskList.add(element)
            }
        }
        println("loadTaskData")
        println(taskList)
        displayTaskData(taskList)
        Toast.makeText(this,"Added!", Toast.LENGTH_SHORT).show()
    }



    fun btnClicked(position: Int, task: String, uid: Int?) {
        var myDialog = Dialog(this)
        myDialog.setContentView(R.layout.layout_popup)
        myDialog.editTextUpdate.setText(task, TextView.BufferType.EDITABLE);
        myDialog.window?.setGravity(Gravity.TOP)
        myDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.show()
        println(position)
        var buttonTaskDelete = myDialog.findViewById(R.id.buttonTaskDelete) as Button
        buttonTaskDelete.setOnClickListener {
            println("buttonTaskDelete")
            deleteDb(taskList[position]){res ->
                if (res){
                    runOnUiThread {
                        val taskView = findViewById<RecyclerView>(R.id.rvTask)
                        taskList.removeAt(position);
                        (taskView.adapter as TaskAdapter).notifyDataSetChanged()
                    }
                }
            }
            myDialog.dismiss()
        }

        var editTextAdd = myDialog.findViewById(R.id.editTextUpdate) as EditText


        var buttonTaskUpdate = myDialog.findViewById(R.id.buttonTaskUpdate) as Button
        buttonTaskUpdate.setOnClickListener {
            if (uid != null) {
                updateDb(uid,"${editTextAdd.text}"){res ->
                    if (res){
                        runOnUiThread {
                            val taskView = findViewById<RecyclerView>(R.id.rvTask)
                            taskList[position].firstName = "${editTextAdd.text}"
                            (taskView.adapter as TaskAdapter).notifyDataSetChanged()
                        }
                    }
                }
            }
            myDialog.dismiss()
        }
    }


}