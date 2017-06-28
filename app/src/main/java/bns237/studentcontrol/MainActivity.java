package bns237.studentcontrol;

import android.app.Dialog;
import android.content.Intent;

import android.graphics.Color;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private static final int Dialog_About = 1;
    private static final int Dialog_Lesson = 2;

    private android.view.ActionMode actionMode;
    private long id ,date;
    private String lesson ;
    private  int Question;
    private int change=100;






    Button save, cancel;
    EditText question;
    Spinner spinner;
    DatePicker datePicker ;
    TableLayout tableLayout;
    TextView tV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        tV = (TextView) findViewById(R.id.tV);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));

        try {
            listRegisters();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Herhangi bir kayıt bulunamadı ! ", Toast.LENGTH_SHORT).show();
        }

    }


    //Bütün kayıtlar listelendi
    private void listRegisters() {

        tableLayout.removeAllViews();

        DataBase db = new DataBase(getApplicationContext());
        List<Student> studentList = new ArrayList<Student>();
        studentList = db.allRegisters();

        //Toplam soru ve günlük soru ortalaması hesaplandı
        long min = studentList.get(studentList.size() - 1).getDate();
        long max = studentList.get(0).getDate();
        Date difference = new Date(max - min);
        int day_difference = ((difference.getYear() % 70) * 365) + (difference.getMonth() * 30) + (difference.getDate() - 1);
        day_difference++;

        int totalQuestion = 0;
        for (Student student : studentList) {

            totalQuestion = totalQuestion + student.getQuestion();

        }
        int averageQuestion = totalQuestion / day_difference;

        if (averageQuestion > change) {

            tV.setText("Tebrikler! Günlük " + change + " soru hedefini aştınız . \n Toplam çözülen soru sayısı : " + totalQuestion
                    + "\n Günlük ortalama soru sayısı: " + averageQuestion);
            tV.setTextColor(Color.WHITE);
            tV.setBackgroundColor(Color.parseColor("#0E3F66"));
        } else {

            tV.setText("Malesef Günlük " + change + " soru hedefinizi gerçekleştiremediniz. \n Toplam çözülen soru sayısı : " + totalQuestion
                    + "\n Günlük ortalama soru sayısı: " + averageQuestion);
            tV.setTextColor(Color.WHITE);
            tV.setBackgroundColor(Color.RED);


        }

        //TableLayouta Veritabanından veriler eklendi
        for (final Student student : studentList) {

            TableRow row = new TableRow(MainActivity.this);
            row.setGravity(Gravity.CENTER);

            TextView tv_date = new TextView(MainActivity.this);
            tv_date.setPadding(2, 2, 2, 2);
            tv_date.setTextColor(Color.WHITE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(student.getDate());
            tv_date.setText(dateFormat.format(date) + "   ");


            TextView tv_lesson = new TextView(MainActivity.this);
            tv_lesson.setPadding(2, 2, 2, 2);
            tv_lesson.setTextColor(Color.WHITE);
            tv_lesson.setText(student.getLesson() + "   ");


            TextView tv_question = new TextView(MainActivity.this);
            tv_question.setPadding(2, 2, 2, 2);
            tv_question.setTextColor(Color.WHITE);
            tv_question.setText(String.valueOf(student.getQuestion()));


            row.addView(tv_date);
            row.addView(tv_lesson);
            row.addView(tv_question);

            tableLayout.addView(row);

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    id = student.getId();
                    if (actionMode != null) {
                        return false;
                    }

                    MyActionModeCallBack callBack = new MyActionModeCallBack();
                    actionMode = startActionMode(callBack);
                    v.setSelected(true);

                    return true;

                }
            });


        }


    }


    //Menü oluşturuldu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            case R.id.add:
                showDialog(Dialog_Lesson);
                return true;


            case R.id.share:
                shareMessage(tV.getText());
                return true;


            case R.id.about:
                showDialog(Dialog_About);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case Dialog_About:
                dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Hakkında");
                dialog.setContentView(R.layout.about);
                break;

            case Dialog_Lesson:

                dialog = getAddDialog();
                break;




            default:
                dialog = null;


        }

        return dialog;
    }


    //Dersler için dialog ekranı eklendi
    private Dialog getAddDialog() {


        LayoutInflater inflater = LayoutInflater.from(this);
        View _layout = inflater.inflate(R.layout.add_lesson, null);

        //add_lesson layoutuna ait componentler tanıtıldı
        save = (Button) _layout.findViewById(R.id.save);
        cancel = (Button) _layout.findViewById(R.id.cancel);
        question = (EditText) _layout.findViewById(R.id.et);
        spinner = (Spinner) _layout.findViewById(R.id.spinner);
        datePicker = (DatePicker) _layout.findViewById(R.id.datePicker);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kayıt Ekle");
        builder.setView(_layout);
        final AlertDialog dialog = builder.create();

        //Kaydet butonuna ait click özellikleri belirlendi
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth() + 1;
                    int year = datePicker.getYear();
                    //Tarih formatı ayarlandı
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                    Date date = null;
                    try {
                        date = df.parse(day + "/" + month + "/" + year);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long _date = date.getTime();
                    //Kayıtlar Spinnera eklendi
                    int position = spinner.getSelectedItemPosition();
                    String lesson = (String) spinner.getItemAtPosition(position);

                    int questionNumber = Integer.valueOf(question.getText().toString());

                    Student student = new Student(lesson, questionNumber, _date);

                    DataBase db = new DataBase(MainActivity.this);

                    //Kayıtın başarılı olup olmadığının kontrolü
                    long id = db.AddRegister(student);
                    if (id == -1) {

                        Toast.makeText(MainActivity.this, "Kayıt sırasında bir hata oluştu !", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(MainActivity.this, "Kayıt İşlemi Başarılı ..", Toast.LENGTH_SHORT).show();
                    }
                    listRegisters();
                    dialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Lütfen kaydetmeden önce  soru sayısı giriniz ..", Toast.LENGTH_SHORT).show();

                }

            }
        });


        //Vazgeç butonuna ait click özellikleri belirlendi
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();

            }
        });


        return dialog;

    }

    //Soru çözüm raporu paylaşıldı
    private void shareMessage(CharSequence message) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(shareIntent, "Paylaşın!!!"));


    }





    class MyActionModeCallBack implements android.view.ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {

            if(item.getItemId() == R.id.delete){ DataBase db = new DataBase(getApplicationContext());
                db.Delete(id);
                listRegisters();
                mode.finish();
                return true;}

            else {

                return false;

            }

        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            actionMode = null;
        }

    }


}
