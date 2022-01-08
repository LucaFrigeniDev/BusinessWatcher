package com.example.businesswatcher.project.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.businesswatcher.R

class CompanyCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_creation)
        setBackButton()
    }

    fun setBackButton(){
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


}