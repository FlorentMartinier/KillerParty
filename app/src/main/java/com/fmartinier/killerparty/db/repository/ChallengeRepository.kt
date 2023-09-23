package com.fmartinier.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.fmartinier.killerparty.db.COLUMN_CHALLENGE_ID
import com.fmartinier.killerparty.db.COLUMN_DESCRIPTION
import com.fmartinier.killerparty.db.COLUMN_ID
import com.fmartinier.killerparty.db.COLUMN_KILLER_ID
import com.fmartinier.killerparty.db.COLUMN_STATE
import com.fmartinier.killerparty.db.MyDatabaseHelper
import com.fmartinier.killerparty.db.TABLE_CHALLENGES
import com.fmartinier.killerparty.db.TABLE_PLAYER_TO_CHALLENGE
import com.fmartinier.killerparty.model.Challenge
import com.fmartinier.killerparty.model.Player
import com.fmartinier.killerparty.model.enums.PlayerToChallengeState

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
        val selectQuery = "SELECT  * FROM $TABLE_CHALLENGES"
        return mapQueryToChallenges(selectQuery)
    }

    fun findActiveFromPlayer(player: Player): Challenge {
        val query = "SELECT c.$COLUMN_ID, c.$COLUMN_DESCRIPTION " +
                "FROM $TABLE_CHALLENGES c " +
                "JOIN $TABLE_PLAYER_TO_CHALLENGE pc on c.$COLUMN_ID=pc.$COLUMN_CHALLENGE_ID " +
                "WHERE pc.$COLUMN_KILLER_ID=${player.id} AND pc.$COLUMN_STATE='${PlayerToChallengeState.IN_PROGRESS}'"

        return mapQueryToChallenges(query).first()
    }

    private fun mapQueryToChallenges(query: String) : MutableList<Challenge> {
        val challenges = mutableListOf<Challenge>()
        val cursor = db.rawQuery(query, null)
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