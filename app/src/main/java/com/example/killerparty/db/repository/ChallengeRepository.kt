package com.example.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.example.killerparty.db.COLUMN_DESCRIPTION
import com.example.killerparty.db.COLUMN_ID
import com.example.killerparty.db.MyDatabaseHelper
import com.example.killerparty.db.TABLE_CHALLENGES
import com.example.killerparty.model.Challenge

class ChallengeRepository(context: Context) {

    private val db = MyDatabaseHelper(context).getDb()

    fun insert(description: String) {
        val values = ContentValues()
        values.put(COLUMN_DESCRIPTION, description)
        db.insert(TABLE_CHALLENGES, null, values)
        println("1 challenge added to database")
    }

    fun deleteById(id: Int) {
        db.delete(TABLE_CHALLENGES, "$COLUMN_ID = $id", null)
    }

    fun findAll(): List<Challenge> {
        val challenges = mutableListOf<Challenge>()
        val selectQuery = "SELECT  * FROM $TABLE_CHALLENGES"

        val cursor = db.rawQuery(selectQuery, null)

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