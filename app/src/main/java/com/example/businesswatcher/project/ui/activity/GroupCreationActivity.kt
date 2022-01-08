package com.example.businesswatcher.project.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.businesswatcher.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_creation)
        setBackButton()
    }

    fun setBackButton(){
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}