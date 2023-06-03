@file:Suppress("DEPRECATION")
package com.example.poprobuiotgadai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup

var answer: String = ""

class ActivityAnswer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)
        val enterButton = findViewById<Button>(R.id.btnCheck)
        val group = findViewById<RadioGroup>(R.id.RadioGroup)

        group.setOnCheckedChangeListener {
            radioGroup: RadioGroup?, checkId: Int ->
            val checkedRadioButton = radioGroup?.findViewById(group.checkedRadioButtonId) as? RadioButton
            checkedRadioButton?.let {
                if (checkedRadioButton.isChecked){
                    enterButton.isEnabled = true
                    answer = checkedRadioButton.text.toString()
                }
            }
        }

        // получаем ранее перемешанный список ответов из MainActivity и генерируем выбор ответов
        val list = intent.getStringArrayListExtra("answerArray")!!
        for ( i in 0 until list.size){
            val radioButton = RadioButton(this)
            radioButton.apply {
                text = list[i]
                id = i
            }
            group.addView(radioButton)
        }

        enterButton.setOnClickListener {
            val intent = Intent(this, MainActivity:: class.java)
            intent.putExtra("answer", answer)
            setResult(RESULT_OK,intent)
            finish()
        }
    }
}