package com.example.poprobuiotgadai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class StatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat)
        val correctTextView = findViewById<TextView>(R.id.correctTextView)
        val wrongTextView = findViewById<TextView>(R.id.wrongTextView)

        val backButton = findViewById<Button>(R.id.backButton)

        // Получаем количество верных ответов из MainActivity
        val correctCount:Int = (intent.getStringExtra("correctCount"))!!.toInt()
        correctTextView.text = correctCount.toString()
        wrongTextView.text = (10 - correctCount).toString()
        backButton.setOnClickListener {
            finish()
        }
    }
}