package com.example.killerparty.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private var db: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        this.db = db
        initializeChallengeTable()
        initializePartyTable()
        initializePlayerTable()
        initializePlayerToChallengeTable()
        initializePlayerToPartyTable()
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHALLENGES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLAYERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLAYER_TO_PARTY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLAYER_TO_CHALLENGE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PARTIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORICS")
        onCreate(db)
    }

    fun getDb(): SQLiteDatabase {
        return db
    }

    private fun initializeChallengeTable() {
        // Script to create table.
        val script = "CREATE TABLE $TABLE_CHALLENGES(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_DESCRIPTION TEXT" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePartyTable() {
        // Script to create table.
        val script = "CREATE TABLE $TABLE_PARTIES(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_STATE TEXT" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePlayerTable() {
        // Script to create table.
        val script = "CREATE TABLE $TABLE_PLAYERS(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_PHONE TEXT," +
                "$COLUMN_STATE TEXT" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePlayerToChallengeTable() {
        // Script to create table.
        val script = "CREATE TABLE $TABLE_PLAYER_TO_CHALLENGE(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_CHALLENGE_ID INTEGER," +
                "$COLUMN_TARGET_ID INTEGER" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePlayerToPartyTable() {
        // Script to create table.
        val script = "CREATE TABLE $TABLE_PLAYER_TO_PARTY(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_PLAYER_ID INTEGER," +
                "$COLUMN_PARTY_ID INTEGER" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }
}