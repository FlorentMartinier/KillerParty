package com.fmartinier.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.fmartinier.killerparty.db.COLUMN_DATE
import com.fmartinier.killerparty.db.COLUMN_ID
import com.fmartinier.killerparty.db.COLUMN_SESSION_ID
import com.fmartinier.killerparty.db.COLUMN_STATE
import com.fmartinier.killerparty.db.COLUMN_WINNER
import com.fmartinier.killerparty.db.MyDatabaseHelper
import com.fmartinier.killerparty.db.TABLE_PARTIES
import com.fmartinier.killerparty.db.executeUpdateQuery
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.model.Player
import com.fmartinier.killerparty.model.enums.PartyState
import java.time.Instant

class PartyRepository(context: Context) {

    private val db = MyDatabaseHelper(context).getDb()

    /**
     * Find the only not started party in database.
     * If none not started party exist, create one
     */
    fun findOrCreate(): Party {
        val selectQuery = "SELECT * " +
                "FROM $TABLE_PARTIES " +
                "WHERE $COLUMN_STATE = '${PartyState.NOT_STARTED.name}' "

        val cursor = db.rawQuery(selectQuery, null)

        // If none party exist, create one
        if (!cursor.moveToFirst()) {
            insertNotStarted()
        }
        cursor.close()

        val cursor2 = db.rawQuery(selectQuery, null)
        // Only one party not started must be available
        val party = if (cursor2.moveToFirst()) {
            Party(
                id = cursor2.getString(0).toInt(),
                date = Instant.parse(cursor2.getString(1)),
                state = PartyState.NOT_STARTED,
                winner = null,
                sessionId = cursor2.getString(4),
            )
        } else {
            throw Exception("Error during party recuperation")
        }
        cursor2.close()
        return party
    }

    fun findAll(): List<Party> {
        val parties = mutableListOf<Party>()
        val selectQuery = "SELECT  * FROM $TABLE_PARTIES"

        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val party = Party(
                    id = cursor.getString(0).toInt(),
                    date = Instant.parse(cursor.getString(1)),
                    state = PartyState.valueOf(cursor.getString(2)),
                    winner = cursor.getString(3),
                    sessionId = cursor.getString(4),
                )
                parties.add(party)
            } while (cursor.moveToNext())
        }
        cursor.close()

        // return note list
        return parties
    }

    fun modifyStateById(id: Int, partyState: PartyState) {
        val values = ContentValues()
        values.put(COLUMN_STATE, partyState.name)

        db.update(TABLE_PARTIES, values, "$COLUMN_ID = $id", null)
    }

    fun modifyDateById(id: Int, date: Instant) {
        val values = ContentValues()
        values.put(COLUMN_DATE, date.toString())

        db.update(TABLE_PARTIES, values, "$COLUMN_ID = $id", null)
    }

    fun modifySessionIdById(id: Int, sessionId: String) {
        val values = ContentValues()
        values.put(COLUMN_SESSION_ID, sessionId.toString())

        db.update(TABLE_PARTIES, values, "$COLUMN_ID = $id", null)
    }

    private fun insertNotStarted() {
        val values = ContentValues()
        values.put(COLUMN_STATE, PartyState.NOT_STARTED.name)
        values.put(COLUMN_DATE, Instant.now().toString())
        db.insert(TABLE_PARTIES, null, values)
        println("1 party added to database")
    }

    fun declareWinner(player: Player, party: Party) {
        val updateQuery = "UPDATE $TABLE_PARTIES " +
                "SET $COLUMN_WINNER='${player.name}' " +
                "WHERE $COLUMN_ID='${party.id}'"

        executeUpdateQuery(db, updateQuery)
    }
}