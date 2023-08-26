package com.fmartinier.killerparty.db

const val DATABASE_NAME = "KillerParty"
const val DATABASE_VERSION = 16

// Tables
const val TABLE_CHALLENGES = "challenges"
const val TABLE_PARTIES = "parties"
const val TABLE_PLAYERS = "players"
const val TABLE_PLAYER_TO_CHALLENGE = "player_to_challenge"
const val TABLE_PLAYER_TO_PARTY = "player_to_party"

// Columns
const val COLUMN_ID = "id"
const val COLUMN_DESCRIPTION = "description"
const val COLUMN_NAME = "name"
const val COLUMN_PHONE = "phone"
const val COLUMN_STATE = "state"
const val COLUMN_CHALLENGE_ID = "challenge_id"
const val COLUMN_KILLER_ID = "killer_id"
const val COLUMN_TARGET_ID = "target_id"
const val COLUMN_PLAYER_ID = "player_id"
const val COLUMN_PARTY_ID = "party_id"
const val COLUMN_DATE = "date"
const val COLUMN_WINNER = "winner"