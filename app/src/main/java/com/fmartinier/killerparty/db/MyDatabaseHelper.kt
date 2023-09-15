package com.fmartinier.killerparty.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fmartinier.killerparty.R
import com.fmartinier.killerparty.services.ChallengeService


class MyDatabaseHelper(
    val context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

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
        db.execSQL(script)

        DEFAULT_CHALLENGES
            .map { context.getString(it) }
            .forEach { insertChallenge(it) }
    }

    private fun initializePartyTable() {
        // Script to create table.
        val script = "CREATE TABLE $TABLE_PARTIES(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_DATE DATE, " +
                "$COLUMN_STATE TEXT, " +
                "$COLUMN_WINNER TEXT" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePlayerTable() {
        // Script to create table.
        val script = "CREATE TABLE ${TABLE_PLAYERS}(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_PHONE TEXT," +
                "$COLUMN_STATE TEXT," +
                "$COLUMN_SCORE INTEGER" +
                ")"
        // Execute script.
        this.db.execSQL(script)
    }

    private fun initializePlayerToChallengeTable() {
        // Script to create table.
        val script = "CREATE TABLE $TABLE_PLAYER_TO_CHALLENGE(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_CHALLENGE_ID INTEGER," +
                "$COLUMN_KILLER_ID INTEGER," +
                "$COLUMN_TARGET_ID INTEGER," +
                "$COLUMN_STATE TEXT" +
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

    private fun insertChallenge(description: String) {
        val values = ContentValues()
        values.put(COLUMN_DESCRIPTION, description)
        db.insert(TABLE_CHALLENGES, null, values)
        println("1 challenge added to database")
    }

    companion object {
        val DEFAULT_CHALLENGES = listOf(
            R.string.challenge_reveal,
            R.string.challenge_sing_patrick_sebastien,
            R.string.challenge_drink_bottoms_up,
            R.string.challenge_congratulate,
            R.string.challenge_talk_philosophy,
            R.string.challenge_show_picture,
            R.string.challenge_kiss,
            R.string.challenge_metal_music,
            R.string.challenge_random_phone,
            R.string.challenge_push_up,
            R.string.challenge_carioca,
            R.string.challenge_joke,
            R.string.challenge_imitate,
            R.string.challenge_cringe_story,
            R.string.challenge_take_beard,
            R.string.challenge_pryapisme,
            R.string.challenge_grimace,
            R.string.challenge_help,
        )
    }
}