package net.kazuhiro1982.nfcsample.timerecorder.recorder;

public class Record {

    private static final String DATE_FORMAT = "%02d/%02d";

    private int id;

    private int date;

    private String start;

    private String end;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public String getHumanDate() {
        int mm = date % 10000 / 100;
        int dd = date % 100;
        return String.format(DATE_FORMAT, mm, dd);
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}