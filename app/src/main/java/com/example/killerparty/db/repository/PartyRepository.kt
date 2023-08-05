package com.example.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.example.killerparty.db.COLUMN_DATE
import com.example.killerparty.db.COLUMN_ID
import com.example.killerparty.db.COLUMN_STATE
import com.example.killerparty.db.MyDatabaseHelper
import com.example.killerparty.db.TABLE_PARTIES
import com.example.killerparty.model.Party
import com.example.killerparty.model.enums.PartyState
import java.time.LocalDate

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
                date = LocalDate.parse(cursor2.getString(1)),
                state = PartyState.NOT_STARTED,
                winner = null,
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
                    date = LocalDate.parse(cursor.getString(1)),
                    state = PartyState.valueOf(cursor.getString(2)),
                    winner = cursor.getString(3),
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

    private fun insertNotStarted() {
        val values = ContentValues()
        values.put(COLUMN_STATE, PartyState.NOT_STARTED.name)
        values.put(COLUMN_DATE, LocalDate.now().toString())
        db.insert(TABLE_PARTIES, null, values)
        println("1 party added to database")
    }
}