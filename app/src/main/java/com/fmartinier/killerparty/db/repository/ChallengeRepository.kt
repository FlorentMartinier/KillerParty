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

    private val db = com.fmartinier.killerparty.db.MyDatabaseHelper(context).getDb()

    fun insert(description: String) {
        val values = ContentValues()
        values.put(com.fmartinier.killerparty.db.COLUMN_DESCRIPTION, description)
        db.insert(com.fmartinier.killerparty.db.TABLE_CHALLENGES, null, values)
        println("1 challenge added to database")
    }

    fun deleteById(id: Int) {
        db.delete(com.fmartinier.killerparty.db.TABLE_CHALLENGES, "${com.fmartinier.killerparty.db.COLUMN_ID} = $id", null)
    }

    fun findAll(): List<Challenge> {
        val selectQuery = "SELECT  * FROM ${com.fmartinier.killerparty.db.TABLE_CHALLENGES}"
        return mapQueryToChallenges(selectQuery)
    }

    fun findActiveFromPlayer(player: Player): Challenge {
        val query = "SELECT c.${com.fmartinier.killerparty.db.COLUMN_ID}, c.${com.fmartinier.killerparty.db.COLUMN_DESCRIPTION} " +
                "FROM ${com.fmartinier.killerparty.db.TABLE_CHALLENGES} c " +
                "JOIN ${com.fmartinier.killerparty.db.TABLE_PLAYER_TO_CHALLENGE} pc on c.${com.fmartinier.killerparty.db.COLUMN_ID} = pc.${com.fmartinier.killerparty.db.COLUMN_CHALLENGE_ID} " +
                "WHERE pc.${com.fmartinier.killerparty.db.COLUMN_KILLER_ID} = '${player.id}' AND pc.${com.fmartinier.killerparty.db.COLUMN_STATE}='${PlayerToChallengeState.IN_PROGRESS}'"

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