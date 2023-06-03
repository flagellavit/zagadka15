package com.example.poprobuiotgadai

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlin.system.exitProcess

val riddles = arrayListOf(
    "Висит на ветке колобок,\n  Блестит его румяный бок.  ",
    "Все о ней боксеры знают,\n  С ней удар свой развивают. \n Хоть она и неуклюжа, \n Но на фрукт похожа…  ",
    "Знают этот фрукт детишки, \n Любят, есть его мартышки. \n  Родом он из жарких стран \n В тропиках растет… ",
    "На ветвях они висели.  Как созрели, посинели. \n Смотрят сверху вниз пугливо, \n Ждут, когда сорвут их… ",
    "Прилетел к нему рой ос- \n Сладок, мягок… ",
    "Желтый цитрусовый плод \n В странах солнечных растет. \n А на вкус кислейший он.  Как зовут его?  ",
    "Он оранжевый и сочный, \n Любит Новый год.  Посмотри под елку-точно, \n Он в подарках ждет! \n Этот рыжий господин \n Вкусный, сладкий… ",
    "Фрукт сей любят обезьянки,  С ним компот я видел в банке.  Это фрукт на букву «м», \n Я его сегодня съем. ",
    "Жарким солнышком согрет.  В шкурку, как в броню одет. \n Удивит собою нас \n Толстокожий…",
    "Надорвали край рубашки,  Вниз посыпались стекляшки.  не собрать их всех назад. \n Что за плод такой?"
)
val answers = arrayListOf(
    "Яблоко",
    "Груша",
    "Банан",
    "Сливы",
    "Абрикос",
    "Лимон",
    "Мандарин",
    "Манго",
    "Ананас",
    "Гранат"
)

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {
    var randomRiddleOrder: ArrayList<Int> = ArrayList()

    private var correctCount: Int = 0
    private var count: Int = 0
    private var correctAnswer: String = ""

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutButtons = findViewById<LinearLayout>(R.id.LayoutButtons)
        val layoutAnswer = findViewById<LinearLayout>(R.id.LayoutAnswer)
        val layoutAnswerText = findViewById<LinearLayout>(R.id.answerTextLayout)
        val layoutButtonsBase = findViewById<LinearLayout>(R.id.LayoutButtonBase)
        val riddleText = findViewById<TextView>(R.id.riddleText)
        val countText = findViewById<TextView>(R.id.countText)
        val answerText = findViewById<TextView>(R.id.answerText)
        val yourAnswerTextView = findViewById<TextView>(R.id.textView)
        val startButton = findViewById<Button>(R.id.startButton)
        val closeButton = findViewById<Button>(R.id.closeButton)
        val riddleButton = findViewById<Button>(R.id.riddleButton)
        val answerButton = findViewById<Button>(R.id.answerButton)
        val statButton = findViewById<Button>(R.id.statButton)

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                answerButton.isEnabled = false
                riddleButton.isEnabled = true
                if (count >= 10) {
                    riddleButton.isEnabled = false
                    answerButton.isEnabled = false
                    statButton.isEnabled = true
                    layoutButtonsBase.visibility = View.VISIBLE
                }
                // обрабатываем переданный из другого Activity ответ пользователя
                if (result.resultCode == RESULT_OK) {
                    val resultText = result.data?.getStringExtra("answer").toString()
                    yourAnswerTextView.isVisible = true
                    answerText.text = resultText

                    // меняем цвет фона ответа в зависимости от правильности
                    if (answerText.text.toString() == correctAnswer) {
                        layoutAnswerText.setBackgroundColor(Color.argb(255, 150, 240, 150))
                        correctCount += 1
                    } else {
                        layoutAnswerText.setBackgroundColor(Color.argb(255, 240, 150, 150))
                    }
                }
            }

        startButton.setOnClickListener {
            layoutAnswer.visibility = View.VISIBLE
            layoutButtons.visibility = View.VISIBLE
            layoutButtonsBase.visibility = View.INVISIBLE
            countText.visibility = View.VISIBLE
            count = 0
            riddleButton.isEnabled = true
            statButton.isEnabled = false
            countText.text = "$count/10"
            layoutAnswerText.setBackgroundColor(0xE6FFE6)
            answerText.text = ""
            correctCount = 0

            // определим случайный порядок для вопросов
            // для этого сгенерируем диапазон от 0 до 9 и перемешаем его
            for (i in 0 until 10) {
                randomRiddleOrder.add(i)
            }
            randomRiddleOrder.shuffle()
        }

        riddleButton.setOnClickListener {
            riddleText.text = riddles[randomRiddleOrder[count]]
            correctAnswer = answers[randomRiddleOrder[count]]

            answerButton.isEnabled = true
            riddleButton.isEnabled = false
            answerText.text = ""
            yourAnswerTextView.isVisible = false
            count += 1
            countText.text = "$count / 10"
            layoutAnswerText.setBackgroundColor(0xE6FFE6)
        }

        answerButton.setOnClickListener {
            val intent = Intent(this, ActivityAnswer::class.java)
            // передаем перемешанный список ответов в ActivityAnswer через intent
            intent.putExtra("answerArray", answers.shuffled().toCollection(ArrayList()))
            launcher?.launch(intent)
        }

        closeButton.setOnClickListener {
            exitProcess(-1)
        }

        statButton.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            intent.putExtra("correctCount", correctCount.toString())
            startActivity(intent)
            riddleText.text = ""
            layoutAnswer.visibility = View.INVISIBLE
            countText.visibility = View.INVISIBLE
        }
    }
}