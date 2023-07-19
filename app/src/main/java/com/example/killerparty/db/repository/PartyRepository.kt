package com.example.killerparty.db.repository

import android.content.ContentValues
import android.content.Context
import com.example.killerparty.db.COLUMN_STATE
import com.example.killerparty.db.MyDatabaseHelper
import com.example.killerparty.db.TABLE_PARTIES
import com.example.killerparty.model.Party
import com.example.killerparty.model.enums.PartyState
import java.lang.Exception

class PartyRepository(context: Context) {

    private val db = MyDatabaseHelper(context).getDb()

    /**
     * Find the only not started party in database.
     * If none not started party exist, create one
     */
    fun findOrCreateNotStartedParty(): Party {
        val selectQuery = "SELECT * " +
                "FROM $TABLE_PARTIES " +
                "WHERE $COLUMN_STATE = '${PartyState.NOT_STARTED.name}' "

        val cursor = db.rawQuery(selectQuery, null)

        // If none party exist, create one
        if (!cursor.moveToFirst()) {
            insertPartyNotStarted()
        }
        cursor.close()

        val cursor2 = db.rawQuery(selectQuery, null)
        // Only one party not started must be available
        val party = if (cursor2.moveToFirst()) {
            Party(
                id = cursor2.getString(0).toInt(),
                state = PartyState.NOT_STARTED
            )
        } else {
            throw Exception("Error during party recuperation")
        }
        cursor2.close()
        return party
    }

    private fun insertPartyNotStarted() {
        val values = ContentValues()
        values.put(COLUMN_STATE, PartyState.NOT_STARTED.name)
        db.insert(TABLE_PARTIES, null, values)
        println("1 party added to database")
    }
}