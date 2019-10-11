package com.example.diaryapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.diaryapp.data.DatabaseManager.DiaryEntry._ID
import com.example.diaryapp.data.DatabaseManager.DiaryEntry.COLUMN_DATE
import com.example.diaryapp.data.DatabaseManager.DiaryEntry.COLUMN_DIARY
import com.example.diaryapp.data.DatabaseManager.DiaryEntry.COLUMN_TITLE
import com.example.diaryapp.data.DatabaseManager.DiaryEntry.TABLE_NAME
import android.database.sqlite.SQLiteOpenHelper

class DiaryDBHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "mydiaries.db"
        private const val DATABASE_VERSION = "1"
        private  const val  SQL_CREATE_DIARY_TABLE ="CREATE TABLE( $TABLE_NAME ) ("+
                "($_ID INTERGER PRIMARY KEY AUTOINCREMENT , )" +
                "($COLUMN_DATE TEXT, )"+
                "($COLUMN_TITLE TEXT, )"+
                "($COLUMN_DIARY TEXT )"

        private const val SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_DIARY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DELETE_ENTRIES)
    }
}