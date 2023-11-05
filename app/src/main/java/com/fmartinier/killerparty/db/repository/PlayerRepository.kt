package com.fmartinier.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.fmartinier.killerparty.db.COLUMN_ID
import com.fmartinier.killerparty.db.COLUMN_KILLER_ID
import com.fmartinier.killerparty.db.COLUMN_NAME
import com.fmartinier.killerparty.db.COLUMN_PARTY_ID
import com.fmartinier.killerparty.db.COLUMN_PHONE
import com.fmartinier.killerparty.db.COLUMN_PLAYER_ID
import com.fmartinier.killerparty.db.COLUMN_SCORE
import com.fmartinier.killerparty.db.COLUMN_STATE
import com.fmartinier.killerparty.db.COLUMN_TARGET_ID
import com.fmartinier.killerparty.db.MyDatabaseHelper
import com.fmartinier.killerparty.db.TABLE_PLAYERS
import com.fmartinier.killerparty.db.TABLE_PLAYER_TO_CHALLENGE
import com.fmartinier.killerparty.db.TABLE_PLAYER_TO_PARTY
import com.fmartinier.killerparty.db.executeUpdateQuery
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.model.Player
import com.fmartinier.killerparty.model.enums.PlayerState
import com.fmartinier.killerparty.model.enums.PlayerToChallengeState

class PlayerRepository(context: Context) {

    private val db = MyDatabaseHelper(context).getDb()

    fun insert(name: String, phone: String, party: Party) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PHONE, phone)
        values.put(COLUMN_STATE, PlayerState.ALIVE.name)
        values.put(COLUMN_SCORE, 0)
        val playerId = db.insert(TABLE_PLAYERS, null, values)
        insertToParty(playerId = playerId.toInt(), partyId = party.id)
        println("1 player added to database")
    }

    fun deleteById(id: Int) {
        db.delete(TABLE_PLAYERS, "$COLUMN_ID = $id", null)
        db.delete(TABLE_PLAYER_TO_PARTY, "$COLUMN_PLAYER_ID = $id", null)
    }

    fun findAllFromParty(party: Party): List<Player> {
        val selectQuery =
            "SELECT p.$COLUMN_ID, p.$COLUMN_NAME, p.$COLUMN_PHONE, p.$COLUMN_STATE, p.$COLUMN_SCORE " +
                    "FROM $TABLE_PLAYER_TO_PARTY pp " +
                    "JOIN $TABLE_PLAYERS p on p.$COLUMN_ID = pp.$COLUMN_PLAYER_ID " +
                    "WHERE pp.$COLUMN_PARTY_ID = ${party.id} "

        return mapQueryToPlayers(selectQuery)
    }

    fun findKillerOf(player: Player): Player {
        val selectQuery =
            "SELECT p.$COLUMN_ID, p.$COLUMN_NAME, p.$COLUMN_PHONE, p.$COLUMN_STATE, p.$COLUMN_SCORE " +
                    "FROM $TABLE_PLAYER_TO_CHALLENGE pc " +
                    "JOIN $TABLE_PLAYERS p on p.$COLUMN_ID=pc.$COLUMN_KILLER_ID " +
                    "WHERE pc.$COLUMN_TARGET_ID = ${player.id} "

        val players = mapQueryToPlayers(selectQuery)
        return if (players.isEmpty()) {
            throw Exception("Le joueur ${player.name} ne possède pas de killer, ce n'est pas normal.")
        } else {
            players.first()
        }
    }

    fun addScore(player: Player, scoreToAdd: Int) {
        val updateQuery = "UPDATE $TABLE_PLAYERS " +
                "SET $COLUMN_SCORE=${player.score + scoreToAdd} " +
                "WHERE $COLUMN_ID=${player.id}"

        executeUpdateQuery(db, updateQuery)
    }

    fun findTargetOf(player: Player): Player {
        val selectQuery =
            "SELECT p.$COLUMN_ID, p.$COLUMN_NAME, p.$COLUMN_PHONE, p.$COLUMN_STATE, p.$COLUMN_SCORE " +
                    "FROM $TABLE_PLAYER_TO_CHALLENGE pc " +
                    "JOIN $TABLE_PLAYERS p on p.$COLUMN_ID=pc.$COLUMN_TARGET_ID " +
                    "WHERE pc.$COLUMN_KILLER_ID=${player.id} AND pc.$COLUMN_STATE='${PlayerToChallengeState.IN_PROGRESS}'"

        val players = mapQueryToPlayers(selectQuery)
        return if (players.isEmpty()) {
            throw Exception("Le joueur ${player.name} ne possède pas de cible, ce n'est pas normal.")
        } else {
            players.first()
        }
    }

    fun kill(player: Player) {
        val updateQuery = "UPDATE $TABLE_PLAYERS " +
                "SET $COLUMN_STATE='${PlayerState.KILLED}' " +
                "WHERE $COLUMN_ID=${player.id}"

        executeUpdateQuery(db, updateQuery)
    }

    fun isThereAWinner(party: Party): Boolean {
        val inLifePlayers = findAllFromParty(party)
            .filter { it.state == PlayerState.ALIVE }

        return if (inLifePlayers.isEmpty()) {
            throw Exception("Aucun joueur n'est encore en vie. Ce n'est pas normal")
        } else {
            inLifePlayers.size == 1
        }
    }

    private fun mapQueryToPlayers(query: String): List<Player> {
        val cursor = db.rawQuery(query, null)
        val players = mutableListOf<Player>()
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val player = Player(
                    id = cursor.getString(0).toInt(),
                    name = cursor.getString(1),
                    phone = cursor.getString(2),
                    state = PlayerState.valueOf(cursor.getString(3)),
                    score = cursor.getInt(4),
                )
                players.add(player)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return players
    }

    private fun insertToParty(playerId: Int, partyId: Int) {
        val values = ContentValues()
        values.put(COLUMN_PLAYER_ID, playerId)
        values.put(COLUMN_PARTY_ID, partyId)
        db.insert(TABLE_PLAYER_TO_PARTY, null, values)
        println("1 playerToParty added to database")
    }
}