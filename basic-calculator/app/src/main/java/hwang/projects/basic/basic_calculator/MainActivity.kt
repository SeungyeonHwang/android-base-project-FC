package hwang.projects.basic.basic_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }

    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }

    private var isOperator = false
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonClicked(v: View) {
        when (v.id) {
            R.id.button0
            -> numberButtonClicked("0")
            R.id.button1
            -> numberButtonClicked("1")
            R.id.button2
            -> numberButtonClicked("2")
            R.id.button3
            -> numberButtonClicked("3")
            R.id.button4
            -> numberButtonClicked("4")
            R.id.button5
            -> numberButtonClicked("5")
            R.id.button6
            -> numberButtonClicked("6")
            R.id.button7
            -> numberButtonClicked("7")
            R.id.button8
            -> numberButtonClicked("8")
            R.id.button9
            -> numberButtonClicked("9")
            R.id.buttonPlus
            -> operatorButtonClicked("+")
            R.id.buttonMinus
            -> operatorButtonClicked("-")
            R.id.buttonMulti
            -> operatorButtonClicked("*")
            R.id.buttonDivider
            -> operatorButtonClicked("/")
            R.id.buttonModule
            -> operatorButtonClicked("%")
        }
    }

    private fun operatorButtonClicked(operator: String) {
        //빈값에 연산자 무시
        if (expressionTextView.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                val text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                //isOperator, hasOperator false -> 숫자만 입력한 상태(한번도 연산자 들어오지 않은 상태)
                expressionTextView.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(expressionTextView.text)
        ssb.setSpan(
            ForegroundColorSpan(
                getColor(R.color.green)
            ),
            expressionTextView.text.length - 1,
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        expressionTextView.text = ssb
        isOperator = true
        hasOperator = true
    }

    private fun numberButtonClicked(number: String) {
        if (isOperator) {
            //split 추가 -> 다음은 숫자
            expressionTextView.append(" ")
        }
        isOperator = false

        val expressionText = expressionTextView.text.split(" ")

        /**
         * Exception
         */
        //XXX % XXX -> X의 length
        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return

            //제일 앞 0
        } else if (expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

        expressionTextView.append(number)
        //TODO : resultTextView에 실시간으로 계산 결과를 넣어야 하는 기능

    }

    fun resultButtonClicked(v: View) {

    }

    fun clearButtonClicked(v: View) {

    }

    fun historyButtonClicked(v: View) {

    }

}