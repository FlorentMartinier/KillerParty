package com.fmartinier.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.fmartinier.killerparty.db.COLUMN_CHALLENGE_ID
import com.fmartinier.killerparty.db.COLUMN_KILLER_ID
import com.fmartinier.killerparty.db.COLUMN_STATE
import com.fmartinier.killerparty.db.COLUMN_TARGET_ID
import com.fmartinier.killerparty.db.TABLE_PLAYER_TO_CHALLENGE
import com.fmartinier.killerparty.db.executeUpdateQuery
import com.fmartinier.killerparty.model.Player
import com.fmartinier.killerparty.model.enums.PlayerToChallengeState

class PlayerToChallengeRepository(context: Context) {

    private val db = com.fmartinier.killerparty.db.MyDatabaseHelper(context).getDb()

    fun insert(killerId: Int, targetId: Int, challengeId: Int) {
        val values = ContentValues()
        values.put(COLUMN_KILLER_ID, killerId)
        values.put(COLUMN_TARGET_ID, targetId)
        values.put(COLUMN_CHALLENGE_ID, challengeId)
        values.put(COLUMN_STATE, PlayerToChallengeState.IN_PROGRESS.name)
        db.insert(TABLE_PLAYER_TO_CHALLENGE, null, values)
        println("1 playerToChallenge added to database")
    }

    /**
     * Achever le challenge en cours dont le joueur est la cible
     */
    fun achieveChallengeWithTarget(player: Player) {
        val updateQuery = "UPDATE $TABLE_PLAYER_TO_CHALLENGE " +
                "SET $COLUMN_STATE='${PlayerToChallengeState.DONE}' " +
                "WHERE $COLUMN_TARGET_ID=${player.id}"

        executeUpdateQuery(db, updateQuery)
    }

    fun modifyChallengeKiller(actualKiller: Player, newKiller: Player) {
        val updateQuery = "UPDATE $TABLE_PLAYER_TO_CHALLENGE " +
                "SET $COLUMN_KILLER_ID=${newKiller.id} " +
                "WHERE $COLUMN_KILLER_ID=${actualKiller.id}"

        executeUpdateQuery(db, updateQuery)
    }

    fun existByKillerAndTarget(killer: Player, target: Player): Boolean {
        val selectQuery = "SELECT * " +
                "FROM $TABLE_PLAYER_TO_CHALLENGE " +
                "WHERE $COLUMN_KILLER_ID=${killer.id} AND $COLUMN_TARGET_ID=${target.id}"

        val cursor = db.rawQuery(selectQuery, null)
        val exist = cursor.moveToFirst()
        cursor.close()
        return exist
    }
}