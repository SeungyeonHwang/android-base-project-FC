package hwang.projects.basic.basic_calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import hwang.projects.basic.basic_calculator.dao.HistoryDao
import hwang.projects.basic.basic_calculator.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    //AppDatabase를 생성할때 historyDao 사용가능하게 만들어 줌
    //추상 클래스 -> 실체 클래스의 공통적인 부분을 추출해 어느정도 규격을 만들어놓은 추상적 클래스(변수/메서드)
    abstract fun historyDao(): HistoryDao
}