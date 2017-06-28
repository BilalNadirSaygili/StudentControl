/*Öğrenci sınıfımız*/

package bns237.studentcontrol;

/**
 * Created by bns on 23.06.2017.
 */

public class Student {
    //Öğrenci sınıfı için gerekli değişkenler tanımlandı
    private String Lesson ;
    private int Question ;
    private long Date ;
    private long Id;


//Kurucu metodlar oluşturuldu
    public Student() {

    }

    public Student(String lesson, int question, long date) {
        setLesson(lesson);
        setQuestion(question);
        setDate(date);
    }

    //Getter ve Setterlar oluşturuldu


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getLesson() {
        return Lesson;
    }

    public void setLesson(String lesson) {
        Lesson = lesson;
    }

    public int getQuestion() {
        return Question;
    }

    public void setQuestion(int question) {
        Question = question;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }
}
