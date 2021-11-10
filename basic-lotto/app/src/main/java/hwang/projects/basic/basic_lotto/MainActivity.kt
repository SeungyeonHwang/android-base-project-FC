package hwang.projects.basic.basic_lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val clearButton: Button by lazy {
        findViewById(R.id.clearButton)
    }
    private val addButton: Button by lazy {
        findViewById(R.id.addButton)
    }
    private val runButton: Button by lazy {
        findViewById(R.id.runButton)
    }
    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }
    private val numberTextVewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.textView1),
            findViewById<TextView>(R.id.textView2),
            findViewById<TextView>(R.id.textView3),
            findViewById<TextView>(R.id.textView4),
            findViewById<TextView>(R.id.textView5),
            findViewById<TextView>(R.id.textView6)
        )
    }

    //Save State(Random)
    private var didRun = false

    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //numberPicker Range Set
        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        //번호 선택
        initAddButton()

        //랜덤
        initRunButton()

        //클리어
        initClearButton()
    }

    //RunButton 속성 초기화 -> return Void
    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()

            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numberTextVewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackGround(number, textView)
            }
        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {

            //예외 처리1 : 자동 번호 발급한 상태
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요", Toast.LENGTH_SHORT).show()
                //setOnClickListener을 return 명시
                return@setOnClickListener
            }

            //예외 처리2 : 6개 이상 선택X
            if (pickNumberSet.size >= 6) {
                Toast.makeText(this, "번호는 6개 까지만 선택 할 수 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //예외 처리3 : 중복 되는 번호
            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호 입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextVewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            setNumberBackGround(numberPicker.value, textView)

            pickNumberSet.add(numberPicker.value)
        }
    }

    /**
     * 색깔 맞추기(숫자에 따라)
     * 앱에 저장되어 있는 것이기 때문에 ContextCompat(context의미)
     */
    private fun setNumberBackGround(number: Int, textView: TextView) {
        when (number) {
            in 1..10 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextVewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun getRandomNumber(): List<Int> {

        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) {
                        //Skip
                        continue
                    }
                    this.add(i)
                }
            }

        numberList.shuffle()

        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)
        return newList.sorted()
    }
}