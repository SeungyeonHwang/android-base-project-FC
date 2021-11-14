package hwang.projects.basic.basic_secretdiary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global.putString
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

//manifests 등록 필요
class DiaryActivity : AppCompatActivity() {

    //main 쓰레드에 연결된 핸들러 작성
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        //defValue : 저장된 값이 없으면 setText
        diaryEditText.setText(detailPreferences.getString("detail", ""))

        //멈칫 할때 저장하는 기능 구현 : 쓰레드 사용
        //Runnable 구현체
        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                //Background처리 이기 때문에 -> 비동기로 저장 apply 넘기는 식으로(commit = false)
                putString("detail", diaryEditText.text.toString())
            }
            Log.d("DiaryActivity", "SAVE!!! ${diaryEditText.text.toString()}")
        }
        //저장이 되지 않아 날아가는걸 방지하기위해 변경이 들어갈때마다 람다 실행 -> 저장 해줌
        diaryEditText.addTextChangedListener {

            Log.d("DiaryActivity", "TextChanged :: $it")
            //handler 쓰레드를 열었을때 , UI -> UI / main 쓰레드
            //새로운(생성한) 쓰레드를 UI쓰레드와 연결 할 필요가 있음 -> main 쓰레드가 아닌 곳에서는 UI체인지를 수행할 수 없음
            //연결해주는 기능을 Handler
            //몇초 이후에 Runnable 실행 하는 처리
            handler.removeCallbacks(runnable) //500mili 0.5초 이전에 아직 실행되지 않고 pending 되어있는 Runnable을 지워준다
            handler.postDelayed(runnable, 500)
        }
    }
}