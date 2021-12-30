package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }
        loadItems()
        //look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        //Attach the adapter to the recycler view to populate view
        recyclerView.adapter = adapter
        //Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        findViewById<Button>(R.id.button).setOnClickListener {
            //Grab the text that the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //Add the string to our ist of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the data adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //Reset Text Field
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save the data that the user has inputted
    //By writing and reading from a file

    //Get the data file we need
    fun getDataFile() : File {
        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }
    //Load the items by reading every line in the data file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch(ioException:IOException){
            ioException.printStackTrace()
        }
    }
    //Save items by writing them to the file
    fun saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}