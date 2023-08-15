package com.fmartinier.killerparty.db

import android.database.sqlite.SQLiteDatabase

fun executeUpdateQuery(db: SQLiteDatabase, query: String) {
    val cursor = db.rawQuery(query, null)
    cursor.moveToFirst()
    cursor.close()
}