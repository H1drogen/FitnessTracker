import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fdmgroup.FitnessTrackerApp.DatabaseHelper


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

    fun insertData(date: String, activity: String, weight: Int?, sets: Int?, reps: Int?): Long {
        val values = ContentValues()
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_WEIGHT, weight)
        values.put(COLUMN_ACTIVITY, activity)
        values.put(COLUMN_SETS, sets)
        values.put(COLUMN_REPS, reps)
//        values.put(COLUMN_DISTANCE, distance)

        val db = writableDatabase
        val id = db.insert(TABLE_ACTIVITY_LOGS, null, values)
        db.close()

        return id
    }

     override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
     }

    @SuppressLint("Range")
    fun getDistinctActivities(): List<String> {
        val activities = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT DISTINCT $COLUMN_ACTIVITY FROM $TABLE_ACTIVITY_LOGS", null)

        try {
            if (cursor.moveToFirst()) {
                do {
                    val activity = cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY))
                    activities.add(activity)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
        }

        return activities
    }

    fun addDummyData() {
        val db = writableDatabase

        // Insert sample records into the activity_logs table
        db.execSQL("INSERT INTO $TABLE_ACTIVITY_LOGS ($COLUMN_ACTIVITY, $COLUMN_WEIGHT, $COLUMN_SETS, $COLUMN_REPS, $COLUMN_DATE) VALUES ('Bench Press', 70.0, 3, 10, '2022-03-12')")
        // Add more sample records as needed

        db.close()
    }
    fun clearAllData() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_ACTIVITY_LOGS")
        db.execSQL("DELETE FROM $TABLE_GOALS")
        // Add more DELETE statements if you have additional tables

        db.close()
    }

    // Function to get activity logs for a specific activity
    @SuppressLint("Range")
    fun getActivityLogsForActivity(activity: String): List<ActivityLog> {
        val logs = mutableListOf<ActivityLog>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ACTIVITY_LOGS WHERE $COLUMN_ACTIVITY = ?", arrayOf(activity))

        try {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                    val activityName = cursor.getString(cursor.getColumnIndex(COLUMN_ACTIVITY))
                    val weight = cursor.getDouble(cursor.getColumnIndex(COLUMN_WEIGHT))
                    val sets = cursor.getInt(cursor.getColumnIndex(COLUMN_SETS))
                    val reps = cursor.getInt(cursor.getColumnIndex(COLUMN_REPS))
                    val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))

                    val activityLog = ActivityLog(id, activityName, weight, sets, reps, date)
                    logs.add(activityLog)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
        }

        return logs
    }
}
