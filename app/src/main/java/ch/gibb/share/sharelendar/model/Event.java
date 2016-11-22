package ch.gibb.share.sharelendar.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aswina.Zizzari on 21.11.2016.
 */
public class Event {
    private Integer id;
    private Date date;
    private String information;
    private SchoolClass schoolClass;


    // -----Konstruktor------
    public Event() {
        super();
    }

    // --------Getter---------
    public int getId() {
        return id;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public Date getDate() {
        return date;
    }

    public String getInformation() {
        return information;
    }

    // --------Setter---------
    public void setId(Integer id) {
        this.id = id;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = simpleDateFormat.format(date);
        String result = "Information: " + information + "\n";
        result = result+ "Am: " + dateString;
        return result;
    }
}
