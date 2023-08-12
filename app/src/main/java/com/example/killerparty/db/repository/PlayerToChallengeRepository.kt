package com.example.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.example.killerparty.db.COLUMN_CHALLENGE_ID
import com.example.killerparty.db.COLUMN_KILLER_ID
import com.example.killerparty.db.COLUMN_STATE
import com.example.killerparty.db.COLUMN_TARGET_ID
import com.example.killerparty.db.MyDatabaseHelper
import com.example.killerparty.db.TABLE_PLAYER_TO_CHALLENGE
import com.example.killerparty.model.Player
import com.example.killerparty.model.PlayerToChallenge
import com.example.killerparty.model.enums.PlayerState
import com.example.killerparty.model.enums.PlayerToChallengeState
import java.lang.Exception

class PlayerToChallengeRepository(context: Context) {

    private val db = MyDatabaseHelper(context).getDb()

    fun insert(killerId: Int, targetId: Int, challengeId: Int) {
        val values = ContentValues()
        values.put(COLUMN_KILLER_ID, killerId)
        values.put(COLUMN_TARGET_ID, targetId)
        values.put(COLUMN_CHALLENGE_ID, challengeId)
        values.put(COLUMN_STATE, PlayerState.IN_LIFE.name)
        db.insert(TABLE_PLAYER_TO_CHALLENGE, null, values)
        println("1 playerToChallenge added to database")
    }

    fun findAll(): List<PlayerToChallenge> {
        val playerToChallenges = mutableListOf<PlayerToChallenge>()
        val selectQuery = "SELECT  * FROM $TABLE_PLAYER_TO_CHALLENGE"

        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val playerToChallenge = PlayerToChallenge(
                    id = cursor.getString(0).toInt(),
                    challengeId = cursor.getString(1).toInt(),
                    killerId = cursor.getString(2).toInt(),
                    targetId = cursor.getString(3).toInt(),
                    state = try {
                        PlayerToChallengeState.valueOf(cursor.getString(4))
                    } catch (e: Exception) {
                        PlayerToChallengeState.EMPTY
                    },
                )
                playerToChallenges.add(playerToChallenge)
            } while (cursor.moveToNext())
        }
        cursor.close()

        // return note list
        return playerToChallenges
    }

    /**
     * Achever le challenge en cours dont le joueur est la cible
     */
    fun achieveChallengeWithTarget(player: Player) {
        val updateQuery = "UPDATE $TABLE_PLAYER_TO_CHALLENGE " +
                "SET $COLUMN_STATE='${PlayerToChallengeState.DONE}' " +
                "WHERE $COLUMN_TARGET_ID=${player.id}"

        val cursor = db.rawQuery(updateQuery, null)
        cursor.close()
    }

    fun modifyChallengeKiller(actualKiller: Player, newKiller: Player) {
        val updateQuery = "UPDATE $TABLE_PLAYER_TO_CHALLENGE " +
                "SET $COLUMN_KILLER_ID='${newKiller.id}' " +
                "WHERE $COLUMN_KILLER_ID=${actualKiller.id}"

        val cursor = db.rawQuery(updateQuery, null)
        cursor.close()
    }

    fun existByKillerAndTarget(killer: Player, target: Player): Boolean {
        val selectQuery = "SELECT  * " +
                "FROM $TABLE_PLAYER_TO_CHALLENGE " +
                "WHERE $COLUMN_KILLER_ID='${killer.id}' AND $COLUMN_TARGET_ID='${target.id}'"

        val cursor = db.rawQuery(selectQuery, null)
        val exist = cursor.moveToFirst()
        cursor.close()
        return exist
    }
}