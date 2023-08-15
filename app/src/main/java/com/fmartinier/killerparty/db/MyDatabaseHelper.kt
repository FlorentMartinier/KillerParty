package com.fmartinier.killerparty.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context,
        com.fmartinier.killerparty.db.DATABASE_NAME, null,
        com.fmartinier.killerparty.db.DATABASE_VERSION
    ) {

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
        db.execSQL("DROP TABLE IF EXISTS ${com.fmartinier.killerparty.db.TABLE_CHALLENGES}")
        db.execSQL("DROP TABLE IF EXISTS ${com.fmartinier.killerparty.db.TABLE_PLAYERS}")
        db.execSQL("DROP TABLE IF EXISTS ${com.fmartinier.killerparty.db.TABLE_PLAYER_TO_PARTY}")
        db.execSQL("DROP TABLE IF EXISTS ${com.fmartinier.killerparty.db.TABLE_PLAYER_TO_CHALLENGE}")
        db.execSQL("DROP TABLE IF EXISTS ${com.fmartinier.killerparty.db.TABLE_PARTIES}")
        onCreate(db)
    }

    fun getDb(): SQLiteDatabase {
        return db
    }

    private fun initializeChallengeTable() {
        // Script to create table.
        val script = "CREATE TABLE ${com.fmartinier.killerparty.db.TABLE_CHALLENGES}(" +
                "${com.fmartinier.killerparty.db.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${com.fmartinier.killerparty.db.COLUMN_DESCRIPTION} TEXT" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePartyTable() {
        // Script to create table.
        val script = "CREATE TABLE ${com.fmartinier.killerparty.db.TABLE_PARTIES}(" +
                "${com.fmartinier.killerparty.db.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${com.fmartinier.killerparty.db.COLUMN_DATE} DATE, " +
                "${com.fmartinier.killerparty.db.COLUMN_STATE} TEXT, " +
                "${com.fmartinier.killerparty.db.COLUMN_WINNER} TEXT" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePlayerTable() {
        // Script to create table.
        val script = "CREATE TABLE ${com.fmartinier.killerparty.db.TABLE_PLAYERS}(" +
                "${com.fmartinier.killerparty.db.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${com.fmartinier.killerparty.db.COLUMN_NAME} TEXT," +
                "${com.fmartinier.killerparty.db.COLUMN_PHONE} TEXT," +
                "${com.fmartinier.killerparty.db.COLUMN_STATE} TEXT" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePlayerToChallengeTable() {
        // Script to create table.
        val script = "CREATE TABLE ${com.fmartinier.killerparty.db.TABLE_PLAYER_TO_CHALLENGE}(" +
                "${com.fmartinier.killerparty.db.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${com.fmartinier.killerparty.db.COLUMN_CHALLENGE_ID} INTEGER," +
                "${com.fmartinier.killerparty.db.COLUMN_KILLER_ID} INTEGER," +
                "${com.fmartinier.killerparty.db.COLUMN_TARGET_ID} INTEGER," +
                "${com.fmartinier.killerparty.db.COLUMN_STATE} TEXT" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePlayerToPartyTable() {
        // Script to create table.
        val script = "CREATE TABLE ${com.fmartinier.killerparty.db.TABLE_PLAYER_TO_PARTY}(" +
                "${com.fmartinier.killerparty.db.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${com.fmartinier.killerparty.db.COLUMN_PLAYER_ID} INTEGER," +
                "${com.fmartinier.killerparty.db.COLUMN_PARTY_ID} INTEGER" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }
}