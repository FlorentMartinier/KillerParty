package com.example.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.example.killerparty.db.COLUMN_CHALLENGE_ID
import com.example.killerparty.db.COLUMN_ID
import com.example.killerparty.db.COLUMN_NAME
import com.example.killerparty.db.COLUMN_PARTY_ID
import com.example.killerparty.db.COLUMN_PHONE
import com.example.killerparty.db.COLUMN_PLAYER_ID
import com.example.killerparty.db.COLUMN_STATE
import com.example.killerparty.db.MyDatabaseHelper
import com.example.killerparty.db.TABLE_PLAYERS
import com.example.killerparty.db.TABLE_PLAYER_TO_CHALLENGE
import com.example.killerparty.db.TABLE_PLAYER_TO_PARTY
import com.example.killerparty.model.Party
import com.example.killerparty.model.Player
import com.example.killerparty.model.enums.PlayerState

class PlayerRepository(context: Context) {

    private val db = MyDatabaseHelper(context).getDb()

    fun insert(name: String, phone: String, party: Party) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_PHONE, phone)
        values.put(COLUMN_STATE, PlayerState.IN_LIFE.name)
        val playerId = db.insert(TABLE_PLAYERS, null, values)
        insertToParty(playerId = playerId.toInt(), partyId = party.id)
        println("1 player added to database")
    }

    fun deleteById(id: Int) {
        db.delete(TABLE_PLAYERS, "$COLUMN_ID = $id", null)
        db.delete(TABLE_PLAYER_TO_PARTY, "$COLUMN_PLAYER_ID = $id", null)
    }

    fun findAllFromParty(party: Party): List<Player> {
        val selectQuery = "SELECT p.$COLUMN_ID, p.$COLUMN_NAME, p.$COLUMN_PHONE, p.$COLUMN_STATE " +
                "FROM $TABLE_PLAYER_TO_PARTY pp " +
                "JOIN $TABLE_PLAYERS p on p.$COLUMN_ID = pp.$COLUMN_PLAYER_ID " +
                "WHERE pp.$COLUMN_PARTY_ID = ${party.id} "

        val players = mutableListOf<Player>()
        val cursor = db.rawQuery(selectQuery, null)

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

        // return note list
        return players
    }

    fun giveChallenge(playerId: Int, challengeId: Int) {
        val values = ContentValues()
        values.put(COLUMN_PLAYER_ID, playerId)
        values.put(COLUMN_CHALLENGE_ID, challengeId)
        db.insert(TABLE_PLAYER_TO_CHALLENGE, null, values)
        println("1 playerToChallenge added to database")
    }

    private fun insertToParty(playerId: Int, partyId: Int) {
        val values = ContentValues()
        values.put(COLUMN_PLAYER_ID, playerId)
        values.put(COLUMN_PARTY_ID, partyId)
        db.insert(TABLE_PLAYER_TO_PARTY, null, values)
        println("1 playerToParty added to database")
    }
}