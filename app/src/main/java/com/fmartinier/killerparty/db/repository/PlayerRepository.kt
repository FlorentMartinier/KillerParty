package com.fmartinier.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.fmartinier.killerparty.db.COLUMN_ID
import com.fmartinier.killerparty.db.COLUMN_KILLER_ID
import com.fmartinier.killerparty.db.COLUMN_NAME
import com.fmartinier.killerparty.db.COLUMN_PARTY_ID
import com.fmartinier.killerparty.db.COLUMN_PHONE
import com.fmartinier.killerparty.db.COLUMN_PLAYER_ID
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

    private val db = com.fmartinier.killerparty.db.MyDatabaseHelper(context).getDb()

    fun insert(name: String, phone: String, party: Party) {
        val values = ContentValues()
        values.put(com.fmartinier.killerparty.db.COLUMN_NAME, name)
        values.put(com.fmartinier.killerparty.db.COLUMN_PHONE, phone)
        values.put(com.fmartinier.killerparty.db.COLUMN_STATE, PlayerState.IN_LIFE.name)
        val playerId = db.insert(com.fmartinier.killerparty.db.TABLE_PLAYERS, null, values)
        insertToParty(playerId = playerId.toInt(), partyId = party.id)
        println("1 player added to database")
    }

    fun deleteById(id: Int) {
        db.delete(com.fmartinier.killerparty.db.TABLE_PLAYERS, "${com.fmartinier.killerparty.db.COLUMN_ID} = $id", null)
        db.delete(com.fmartinier.killerparty.db.TABLE_PLAYER_TO_PARTY, "${com.fmartinier.killerparty.db.COLUMN_PLAYER_ID} = $id", null)
    }

    fun findAllFromParty(party: Party): List<Player> {
        val selectQuery = "SELECT p.${com.fmartinier.killerparty.db.COLUMN_ID}, p.${com.fmartinier.killerparty.db.COLUMN_NAME}, p.${com.fmartinier.killerparty.db.COLUMN_PHONE}, p.${com.fmartinier.killerparty.db.COLUMN_STATE} " +
                "FROM ${com.fmartinier.killerparty.db.TABLE_PLAYER_TO_PARTY} pp " +
                "JOIN ${com.fmartinier.killerparty.db.TABLE_PLAYERS} p on p.${com.fmartinier.killerparty.db.COLUMN_ID} = pp.${com.fmartinier.killerparty.db.COLUMN_PLAYER_ID} " +
                "WHERE pp.${com.fmartinier.killerparty.db.COLUMN_PARTY_ID} = ${party.id} "

        return mapQueryToPlayers(selectQuery)
    }

    fun findKillerOf(player: Player): Player {
        val selectQuery = "SELECT p.${com.fmartinier.killerparty.db.COLUMN_ID}, p.${com.fmartinier.killerparty.db.COLUMN_NAME}, p.${com.fmartinier.killerparty.db.COLUMN_PHONE}, p.${com.fmartinier.killerparty.db.COLUMN_STATE} " +
                "FROM ${com.fmartinier.killerparty.db.TABLE_PLAYER_TO_CHALLENGE} pc " +
                "JOIN ${com.fmartinier.killerparty.db.TABLE_PLAYERS} p on p.${com.fmartinier.killerparty.db.COLUMN_ID}=pc.${com.fmartinier.killerparty.db.COLUMN_KILLER_ID} " +
                "WHERE pc.${com.fmartinier.killerparty.db.COLUMN_TARGET_ID} = ${player.id} "

        val players = mapQueryToPlayers(selectQuery)
        return if (players.isEmpty()) {
            throw Exception("Le joueur ${player.name} ne possède pas de killer, ce n'est pas normal.")
        } else {
            players.first()
        }
    }

    fun findTargetOf(player: Player): Player {
        val selectQuery = "SELECT p.${com.fmartinier.killerparty.db.COLUMN_ID}, p.${com.fmartinier.killerparty.db.COLUMN_NAME}, p.${com.fmartinier.killerparty.db.COLUMN_PHONE}, p.${com.fmartinier.killerparty.db.COLUMN_STATE} " +
                "FROM ${com.fmartinier.killerparty.db.TABLE_PLAYER_TO_CHALLENGE} pc " +
                "JOIN ${com.fmartinier.killerparty.db.TABLE_PLAYERS} p on p.${com.fmartinier.killerparty.db.COLUMN_ID}=pc.${com.fmartinier.killerparty.db.COLUMN_TARGET_ID} " +
                "WHERE pc.${com.fmartinier.killerparty.db.COLUMN_KILLER_ID}=${player.id} AND pc.${com.fmartinier.killerparty.db.COLUMN_STATE}='${PlayerToChallengeState.IN_PROGRESS}'"

        val players = mapQueryToPlayers(selectQuery)
        return if (players.isEmpty()) {
            throw Exception("Le joueur ${player.name} ne possède pas de target, ce n'est pas normal.")
        } else {
            players.first()
        }
    }

    fun kill(player: Player) {
        val updateQuery = "UPDATE ${com.fmartinier.killerparty.db.TABLE_PLAYERS} " +
                "SET ${com.fmartinier.killerparty.db.COLUMN_STATE}='${PlayerState.KILLED}' " +
                "WHERE ${com.fmartinier.killerparty.db.COLUMN_ID}=${player.id}"

        com.fmartinier.killerparty.db.executeUpdateQuery(db, updateQuery)
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
                )
                players.add(player)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return players
    }

    private fun insertToParty(playerId: Int, partyId: Int) {
        val values = ContentValues()
        values.put(com.fmartinier.killerparty.db.COLUMN_PLAYER_ID, playerId)
        values.put(com.fmartinier.killerparty.db.COLUMN_PARTY_ID, partyId)
        db.insert(com.fmartinier.killerparty.db.TABLE_PLAYER_TO_PARTY, null, values)
        println("1 playerToParty added to database")
    }
}