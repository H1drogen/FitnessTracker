import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class ActivityLogDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fitness_app.db"
        private const val DATABASE_VERSION = 1

        // Table and column names for activity table
        private const val TABLE_ACTIVITY_LOGS = "activity_logs"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_ACTIVITY = "activity"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_SETS = "sets"
        private const val COLUMN_REPS = "reps"
        private const val COLUMN_DATE = "date"

        // Table and column names for goal table
        private const val TABLE_GOALS = "goals"
        private const val COLUMN_GOAL_ID = "_id"
        private const val COLUMN_GOAL = "goal"
        private const val COLUMN_GOAL_DATE = "date"
    }

    // SQL statement to create the activity_logs table
    private val SQL_CREATE_ACTIVITY_LOGS_TABLE = """
        CREATE TABLE $TABLE_ACTIVITY_LOGS (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_ACTIVITY TEXT NOT NULL,
            $COLUMN_WEIGHT REAL,
            $COLUMN_SETS INTEGER,
            $COLUMN_REPS INTEGER,
            $COLUMN_DATE DATE
        )
    """.trimIndent()

    // SQL statement to create the goals table
    private val SQL_CREATE_GOALS_TABLE = """
        CREATE TABLE $TABLE_GOALS (
            $COLUMN_GOAL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_GOAL TEXT NOT NULL,
            $COLUMN_GOAL_DATE DATE
        )
    """.trimIndent()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ACTIVITY_LOGS_TABLE)
        db.execSQL(SQL_CREATE_GOALS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}
