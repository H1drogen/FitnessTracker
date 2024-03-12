package com.fdmgroup.FitnessTrackerApp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "FitnessGoals"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "FitnessGoals"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_ACTIVITY = "activity"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_SETS = "sets"
        private const val COLUMN_REPS = "reps"
        private const val COLUMN_DISTANCE = "distance"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_ACTIVITY TEXT, " +
                "$COLUMN_WEIGHT INTEGER, " +
                "$COLUMN_SETS INTEGER, " +
                "$COLUMN_REPS INTEGER, " +
                "$COLUMN_DISTANCE INTEGER)"
                )
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(date: String, activity: String, weight: Int?, sets: Int?, reps: Int?, distance: Int?): Long {
        val values = ContentValues()
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_WEIGHT, weight)
        values.put(COLUMN_ACTIVITY, activity)
        values.put(COLUMN_SETS, sets)
        values.put(COLUMN_REPS, reps)
        values.put(COLUMN_DISTANCE, distance)

        val db = writableDatabase
        val id = db.insert(TABLE_NAME, null, values)
        db.close()

        return id
    }
}