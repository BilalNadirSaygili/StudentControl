/**
 * Veritabanımız
 */

package bns237.studentcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bns on 23.06.2017.
 */

public class DataBase extends SQLiteOpenHelper {

    //Veritabanı  için gerekli tanımlamalar yapıldı

    private static final String DATABASE_NAME = "my_dataBase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "student_control_table";

    private static final String ID = "_id";
    private static final String LESSON = "lesson";
    private static final String QUESTION = "question";
    private static final String DATE = "date";


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Veritabanı oluşturuldu
    @Override
    public void onCreate(SQLiteDatabase db) {


        String createTable = "CREATE TABLE " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LESSON + " TEXT, " +
                QUESTION + " INTEGER NOT NULL, " +
                DATE + " INTEGER NOT NULL);";

        db.execSQL(createTable);

    }

    //Veritabanı güncellendi

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);

    }

   //Kayıtlar veritabanına eklendi (eğer kayıt başarısız  olursa long türünde bir -1 değeri geri döndürülür)
    public long AddRegister(Student student) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(LESSON, student.getLesson());
        contentValues.put(QUESTION, student.getQuestion());
        contentValues.put(DATE, student.getDate());

        long id = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return id;
    }
   //Kayıtlat listelendi
    public List<Student> allRegisters() {
     SQLiteDatabase db = this.getReadableDatabase();

        String [] columns = new String[]{LESSON , QUESTION ,DATE ,ID};

        Cursor cursor = db.query(TABLE_NAME ,columns ,null,null,null,null,DATE+" desc");

        int lessonArrayNumber = cursor.getColumnIndex(LESSON);
        int questionArrayNumber = cursor.getColumnIndex(QUESTION);
        int dateArrayNumber = cursor.getColumnIndex(DATE);
        int  idArrayNumber = cursor.getColumnIndex(ID);
        List<Student> studentList = new ArrayList<Student>();

        for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext()){

            Student student = new Student();
            student.setLesson(cursor.getString(lessonArrayNumber) );
            student.setQuestion(cursor.getInt(questionArrayNumber) );
            student.setDate(cursor.getLong(dateArrayNumber) );
            student.setId(cursor.getLong(idArrayNumber));

            studentList.add(student);
        }
      db.close();

    return studentList;
    }

    public void Delete(long id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME ,ID+"="+id ,null );
        db.close();
    }







}
