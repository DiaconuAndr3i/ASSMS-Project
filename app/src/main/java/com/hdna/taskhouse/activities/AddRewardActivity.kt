package com.hdna.taskhouse.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.hdna.taskhouse.R
import com.hdna.taskhouse.models.Rewards
import com.hdna.taskhouse.util.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

class AddRewardActivity : AppCompatActivity() {

    lateinit var submitImageView : ImageView
    lateinit var rewardTitleEditText: EditText
    lateinit var rewardCostEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reward)
        initUi()
    }

    private fun initUi() {
        submitImageView = findViewById(R.id.addRewardCompleteImageView)
        rewardTitleEditText = findViewById(R.id.addRewardTitleEditText)
        rewardCostEditText = findViewById(R.id.addRewardCostEditText)
        submitImageView.setOnClickListener {
            submitReward()
        }
    }

    private fun submitReward() {
        val title = rewardTitleEditText.text.toString()
        val cost = rewardCostEditText.text.toString().toInt()
        val reward = Rewards(title,cost)
        var repository = BaseRepository(this)
        val scope = CoroutineScope(newSingleThreadContext("mainForce"))
        runBlocking {
            scope.launch { repository.addReward(reward)}
        }
        Toast.makeText(this,"Task created",Toast.LENGTH_SHORT).show()
        finish()
    }
}