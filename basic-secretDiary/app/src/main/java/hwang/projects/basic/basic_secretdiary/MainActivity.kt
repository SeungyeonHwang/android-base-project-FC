package hwang.projects.basic.basic_secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    //View가 전부 그려지지 않았기 때문에 Lazy하게 선언
    private val numberPicker1: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    //비밀번호 변경할 때 다른 작업 못하도록 하는 Flag
    private var changePasswordMode = false

    //View가 전부 그려지는 시점 -> OnCreate가 되는 시점에서 View에 접근 해야 되기 때문에 lazy
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //접근(Lazy 초기화), View에 대해서 초기화 해주지 않으면 초기화 되는 시점이 NumberPicker를 사용하는 시점이 되어 버림
        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /**
             * 패스워드를 기기에 저장하는 방법
             * 1. 로컬 DB
             * 2. 파일에 적는 방법
             *  - Preference
             *      - Key-Value 관리
             */
            // App의 간단한 DB 역할을 하는 SharedPreferences, 다른 앱 사용 불가
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            //init
            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                // 패스워드 성공
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                // 패스워드 실패
                showErrorAlertDialog()
            }
        }
        changePasswordButton.setOnClickListener {

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser =
                "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (changePasswordMode) {
                //번호를 저장하는 기능
                passwordPreferences.edit(true) {
                    //저장 -> commit(저장될때까지 UI 정지)/apply(다음작업 진행 비동기적으로 저장)
                    //가벼운 작업이기때문에 commit
                    putString("password", passwordFromUser)
                }
                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)

            } else {
                //changePasswordMode가  :: 비밀번호가 맞는지를 체크
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    // 패스워드 성공 -> 비밀번호 변경 모드
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해 주세요", Toast.LENGTH_SHORT).show()
                    changePasswordButton.setBackgroundColor(Color.RED)
                } else {
                    // 패스워드 실패
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!")
            .setMessage("비밀번호가 잘못되었습니다.")
            //인자 2개 : 명시적 람다, 불필요 코드 _ 처리
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}