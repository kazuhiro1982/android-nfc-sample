package net.kazuhiro1982.nfcsample.timerecorder.recorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static net.kazuhiro1982.nfcsample.timerecorder.recorder.TimeRecordHelper.COLUMN;
import static net.kazuhiro1982.nfcsample.timerecorder.recorder.TimeRecordHelper.TABLE_NAME;

public class TimeRecorder {

    private TimeRecordHelper helper;

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public TimeRecorder(Context context) {
        helper = new TimeRecordHelper(context);
    }

    public void clockIn() {
        Date d = new Date();
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            Record record = getRecordOnToday(db, d);
            if (record == null) {
                record = new Record();
                record.setDate(Integer.parseInt(DATE_FORMAT.format(d)));
                record.setStart(TIME_FORMAT.format(d));
                insertRecord(db, record);
            } else {
                record.setStart(TIME_FORMAT.format(d));
                updateRecord(db, record);
            }
        } finally {
            db.close();
        }
    }

    public void clockOut() {
        Date d = new Date();
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            Record record = getRecordOnToday(db, d);
            if (record == null) {
                record = new Record();
                record.setDate(Integer.parseInt(DATE_FORMAT.format(d)));
                record.setEnd(TIME_FORMAT.format(d));
                insertRecord(db, record);
            } else {
                record.setEnd(TIME_FORMAT.format(d));
                updateRecord(db, record);
            }
        } finally {
            db.close();
        }
    }

    public List<Record> getRecordsOnThisMonth() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        cal.set(Calendar.DATE, 1);
        Date start = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        Date end = cal.getTime();

        try {
            return getRecordOnPeriod(db, start, end);
        } finally {
            db.close();
        }
    }

    private List<Record> getRecordOnPeriod(SQLiteDatabase db, Date start, Date end) {

        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMN.names(COLUMN.ID, COLUMN.DATE, COLUMN.START, COLUMN.END),
                COLUMN.DATE.name + " BETWEEN ? AND ?",
                new String[]{DATE_FORMAT.format(start), DATE_FORMAT.format(end)},
                null,
                null,
                null);

        List<Record> records = new ArrayList<Record>();

        while (cursor.moveToNext()) {
            records.add(convertRecord(cursor));
        }

        return records;

    }

    private Record getRecordOnToday(SQLiteDatabase db, Date d) {

        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMN.names(COLUMN.ID, COLUMN.DATE, COLUMN.START, COLUMN.END),
                COLUMN.DATE.name + " = ?",
                new String[]{DATE_FORMAT.format(d)},
                null,
                null,
                null);

        if (!cursor.moveToFirst()) {
            return null;
        }

        return convertRecord(cursor);
    }

    private Record convertRecord(Cursor cursor) {
        Record record = new Record();
        record.setId(cursor.getInt(0));
        record.setDate(cursor.getInt(1));
        record.setStart(cursor.getString(2));
        record.setEnd(cursor.getString(3));
        return record;
    }

    private long insertRecord(SQLiteDatabase db, Record record) {
        ContentValues values = new ContentValues();
        values.put(COLUMN.DATE.name, record.getDate());
        values.put(COLUMN.START.name, record.getStart());
        values.put(COLUMN.END.name, record.getEnd());
        return db.insert(TABLE_NAME, null, values);
    }

    private int updateRecord(SQLiteDatabase db, Record record) {
        ContentValues values = new ContentValues();
        values.put(COLUMN.DATE.name, record.getDate());
        values.put(COLUMN.START.name, record.getStart());
        values.put(COLUMN.END.name, record.getEnd());
        return db.update(TABLE_NAME, values, COLUMN.ID.name + " = ?", new String[]{String.valueOf(record.getId())});
    }
}
