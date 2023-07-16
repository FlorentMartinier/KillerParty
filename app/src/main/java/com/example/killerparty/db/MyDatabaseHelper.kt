package com.example.killerparty.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.killerparty.model.Challenge


class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        println("on créé la BDD !")
        // Script to create table.
        val script = "CREATE TABLE $TABLE_CHALLENGES(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_DESCRIPTION TEXT" +
                ")"
        // Execute script.
        db.execSQL(script)
        addChallenge("test challenge 1")
        addChallenge("test challenge 2")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHALLENGES")
        onCreate(db)
    }

    fun addChallenge(description: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DESCRIPTION, description)
        db.insert(TABLE_CHALLENGES, null, values)
        println("1 challenge added to database")
    }

    fun deleteChallengeById(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_CHALLENGES, "id = $id", null)
    }

    fun getAllChallenges(): List<Challenge> {
        println("getAllChallenges")
        val challenges = mutableListOf<Challenge>()
        // Select All Query
        val selectQuery = "SELECT  * FROM $TABLE_CHALLENGES"

        val cursor = writableDatabase.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val challenge = Challenge(cursor.getString(0).toInt(), cursor.getString(1))
                challenges.add(challenge)
            } while (cursor.moveToNext())
        }
        cursor.close()

        // return note list
        return challenges
    }
}