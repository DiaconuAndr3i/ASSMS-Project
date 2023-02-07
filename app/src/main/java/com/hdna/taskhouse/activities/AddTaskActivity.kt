package com.hdna.taskhouse.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.hdna.taskhouse.R
import com.hdna.taskhouse.models.PrimaryTask
import com.hdna.taskhouse.util.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import java.util.*


class AddTaskActivity : AppCompatActivity() {

    lateinit var taskTitleEditText: EditText
    lateinit var startDateSelectorFieldTv : TextView
    lateinit var endDateSelectorFieldTv : TextView
    lateinit var spinnerAddTask : Spinner
    lateinit var pointsEditText: EditText
    lateinit var addTaskCompleteImageView: ImageView
    lateinit var spinnerArrayAdapter : ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        initUi()

    }

    private fun initUi() {

        taskTitleEditText = findViewById(R.id.addTaskEditTextTitle)
        pointsEditText = findViewById(R.id.addRewardPointsEditText)

        spinnerAddTask = findViewById<Spinner>(R.id.assignToSpinner)

        startDateSelectorFieldTv = findViewById<TextView>(R.id.startDateSelectorFieldTv)
        endDateSelectorFieldTv = findViewById<TextView>(R.id.endDateSelectorFieldTv)
        addTaskCompleteImageView = findViewById(R.id.addTaskCompleteImageView)


        startDateSelectorFieldTv.setOnClickListener {
            datePick(startDateSelectorFieldTv)
        }

        endDateSelectorFieldTv.setOnClickListener {
            datePick(endDateSelectorFieldTv)
        }

        addTaskCompleteImageView.setOnClickListener {
            submitTask()
        }

        val str: ArrayList<String> = arrayListOf("copil1", "copil2")

        spinnerArrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, str
        )
        spinnerArrayAdapter.notifyDataSetChanged()
        var userNiceNames : ArrayList<String> = arrayListOf()
        val scope = CoroutineScope(newSingleThreadContext("mainForce"))
        var repository = BaseRepository(this)
        runBlocking {
            scope.launch { userNiceNames = repository.getFamilyNiceNames()
                Log.d("userNiceNames1",userNiceNames.toString())
                runOnUiThread {
                    for(i in userNiceNames)
                    {
                        spinnerArrayAdapter.add(i)
                    }
                }
                }

        }

        Log.d("userNiceNames",userNiceNames.toString())
        setSpinner(spinnerAddTask,userNiceNames)

    }

    private fun submitTask() {
        var title = taskTitleEditText.text.toString()
        var startDate = startDateSelectorFieldTv.text.toString()
        var endDate = endDateSelectorFieldTv.text.toString()
        var asignedTo = spinnerAddTask.selectedItem.toString()
        var points = pointsEditText.text.toString().toInt()

        var submitTask = PrimaryTask(title, startDate,endDate,asignedTo,points)
        var repository = BaseRepository(this)
        val scope = CoroutineScope(newSingleThreadContext("mainForce"))
        runBlocking {
            scope.launch { repository.addTask(submitTask)

            }
        }
        Toast.makeText(this,"Task created",Toast.LENGTH_SHORT).show()
        finish()




    }

    private fun datePick(fieldTv: TextView?) {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(

            this,
            { _, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our text view.
                fieldTv!!.text =
                    (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun setSpinner(spinner: Spinner?,users: ArrayList<String>) {
        val str: ArrayList<String> = arrayListOf("copil1", "copil2")
        spinnerArrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, users
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = spinnerArrayAdapter
    }
}