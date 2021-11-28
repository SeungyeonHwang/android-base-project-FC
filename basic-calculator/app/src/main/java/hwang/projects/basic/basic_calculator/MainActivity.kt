package hwang.projects.basic.basic_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }

    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }

    private val historyLayout: View by lazy {
        findViewById<View>(R.id.historyLayout)
    }

    private val historyLinearLayout: View by lazy {
        findViewById<View>(R.id.historyLinearLayout)
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
        resultTextView.text = calculateExpression()
    }

    //계산 결과 반환
    private fun calculateExpression(): String {
        val expressionTexts = expressionTextView.text.split(" ")

        if (hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }
        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    //유저의 액션 -> 예외 처리
    fun resultButtonClicked(v: View) {
        val expressionTexts = expressionTextView.text.split(" ")

        //예외 처리1 : 빈 값 or 숫자만 입력
        if (expressionTextView.text.isEmpty() || expressionTexts.size == 1) {
            return
        }

        //예외 처리2 : 숫자 연산자 만 입력
        if (expressionTexts.size != 3 && hasOperator) {
            Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        //예외 처리3 : 숫자이외
        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()

        resultTextView.text = ""
        expressionTextView.text = resultText //결과를 띄워준다

        isOperator = false
        hasOperator = false
    }

    fun clearButtonClicked(v: View) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        hasOperator = false
    }

    fun historyButtonClicked(v: View) {
        historyLayout.isVisible = true
        //TODO 디비에서 모든 기록 가져오기
        //TODO 뷰에 모든 기록 할당하기
    }

    fun closeHistoryButtonClicked(v: View) {
        historyLayout.isVisible = false
    }

    fun historyClearButtonClicked(v: View) {
        //TODO 디비에서 모든 기록 삭제
        //TODO 뷰에서 모든 기록 삭제
    }


}

//확장함수 구현
fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        return true
    } catch (e: NumberFormatException) {
        false
    }
}