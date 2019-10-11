package com.example.diaryapp

import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.contentValuesOf
import com.example.diaryapp.data.DatabaseManager.DiaryEntry.COLUMN_DATE
import com.example.diaryapp.data.DatabaseManager.DiaryEntry.COLUMN_DIARY
import com.example.diaryapp.data.DatabaseManager.DiaryEntry.COLUMN_TITLE
import com.example.diaryapp.data.DatabaseManager.DiaryEntry.TABLE_NAME
import com.example.diaryapp.data.DatabaseManager.DiaryEntry._ID
import com.example.diaryapp.data.DiaryDBHelper
import kotlinx.android.synthetic.main.activity_new_diary.*
import java.text.SimpleDateFormat
import java.util.*

class NewDiary : AppCompatActivity() {

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_diary)
        id = intent.getIntExtra("idofRow",0)
        if (id != 0){
            readDiary(id)
        }
        Log.d("NewDiary","the past id is $id")
        val currentDate = SimpleDateFormat("EEE, d MMM yyyy")
        current_date_diary.text = currentDate.format(Date())
    }

    private fun readDiary(id: Int) {
        val mDBHelper = DiaryDBHelper(this)
        val db = mDBHelper.readableDatabase
        val projection = arrayOf(COLUMN_DATE, COLUMN_TITLE, COLUMN_DIARY)

        val selection = "$_ID = ?"
        val selectionArgs = arrayOf("$id")

        val cursor : Cursor  = db.query(
             TABLE_NAME,
            projection,
            selection,
            selectionArgs,null,null,null
        )
        val dateColumIndex = cursor.getColumnIndexOrThrow(COLUMN_DATE)
        val titleColumIndex = cursor.getColumnIndexOrThrow(COLUMN_TITLE)
        val diaryColumIndex = cursor.getColumnIndexOrThrow(COLUMN_DIARY)

        while (cursor.moveToNext()){
            val currentDate = cursor.getString(dateColumIndex)
            val currentTitle = cursor.getString(titleColumIndex)
            val currentDiary = cursor.getString(diaryColumIndex)

            current_date_diary.text = currentDate
            title_diary.setText(currentTitle)
            diary_text.setText(currentDiary)
        }
        cursor.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val infalter : MenuInflater = menuInflater
        infalter.inflate(R.menu.action_bar_menu,menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.save_diary ->{

                if (id == 0 ){
                    insertDiary()
                }else {
                    updateDiary(id)
                }
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun updateDiary(id: Int) {
        val mDBHelper = DiaryDBHelper(this)
        val db = mDBHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title_diary.text.toString())
            put(COLUMN_DIARY, diary_text.text.toString())
        }
        db.update(TABLE_NAME,values, "$_ID  = $id",null)
    }

    private fun insertDiary() {
        val dateString  = current_date_diary.text
        val titleString = title_diary.text.toString().trim(){it <= ' '}
        val diaryString = diary_text.text.toString().trim(){it <= ' '}
        val mDBHelper = DiaryDBHelper(this)
        val db = mDBHelper.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_DATE, dateString.toString())
            put(COLUMN_TITLE, titleString)
            put(COLUMN_DIARY, diaryString)
        }

        val rowID = db.insert(TABLE_NAME,null,values)
        if (rowID.equals(-1)){
            Toast.makeText(this,"problem in inserting new diary",Toast.LENGTH_SHORT)
        }else{
            Toast.makeText(this,"Sucessful",Toast.LENGTH_SHORT)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (id == 0 ){
            insertDiary()
        }else {
            updateDiary(id)
        }
    }
}
